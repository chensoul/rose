package com.chensoul.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class CanalRunner {
	int BATCH_SIZE = 5 * 1024;
	@Resource
	private CanalConnector connector;

	private static void printEntry(List<CanalEntry.Entry> entrys) {
		for (CanalEntry.Entry entry : entrys) {
			//开启/关闭事务的实体类型，跳过
			if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
				continue;
			}
			CanalEntry.RowChange rowChage;
			try {
				rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
			} catch (Exception e) {
				throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
			}
			//获取操作类型：insert/update/delete类型
			CanalEntry.EventType eventType = rowChage.getEventType();
			//打印Header信息
			System.out.println(String.format("================》; binlog[%s:%s] , name[%s,%s] , eventType : %s",
				entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
				entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
				eventType));
			//判断是否是DDL语句
			if (rowChage.getIsDdl()) {
				System.out.println("================》;isDdl: true,sql:" + rowChage.getSql());
			}
			//获取RowChange对象里的每一行数据，打印出来
			for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
				//如果是删除语句
				if (eventType == CanalEntry.EventType.DELETE) {
					printColumn(rowData.getBeforeColumnsList());
					//如果是新增语句
				} else if (eventType == CanalEntry.EventType.INSERT) {
					printColumn(rowData.getAfterColumnsList());
					//如果是更新的语句
				} else {
					//变更前的数据
					System.out.println("------->; before");
					printColumn(rowData.getBeforeColumnsList());
					//变更后的数据
					System.out.println("------->; after");
					printColumn(rowData.getAfterColumnsList());
				}
			}
		}
	}

	private static void printColumn(List<CanalEntry.Column> columns) {
		for (CanalEntry.Column column : columns) {
			System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
		}
	}

	@PostConstruct
	public void connect() {
		connector.connect();
		connector.subscribe(null);
		connector.rollback();
	}

	@Async
	@Scheduled(initialDelayString = "${canal.promotion.initialDelay:2000}", fixedDelayString = "${canal.promotion.fixedDelay:2000}")
	public void processData() {
		try {
			if (!connector.checkValid()) {
				log.warn("与Canal服务器的连接失效！！！重连，下个周期再检查数据变更");
				this.connect();
			} else {
				Message message = connector.getWithoutAck(BATCH_SIZE);
				long batchId = message.getId();
				int size = message.getEntries().size();
				if (batchId == -1 || size == 0) {
					log.info("本次[{}]没有检测到数据更新。", batchId);
				} else {
					log.info("本次[{}]数据共有[{}]次更新需要处理", batchId, size);
					printEntry(message.getEntries());

					connector.ack(batchId); // 提交确认
					log.info("本次[{}]处理Canal同步数据完成", batchId);
				}
			}
		} catch (Exception e) {
			log.error("处理Canal同步数据失效，请检查：", e);
		}

	}

	@PreDestroy
	public void disConnect() {
		connector.disconnect();
	}
}

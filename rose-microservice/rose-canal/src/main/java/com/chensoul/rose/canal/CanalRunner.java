/*
 * Copyright © 2025 Chensoul, Inc. (ichensoul@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CanalRunner {

    int BATCH_SIZE = 5 * 1024;

    @Resource
    private CanalConnector connector;

    private static void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            // 开启/关闭事务的实体类型，跳过
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                    || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException(
                        "ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }
            CanalEntry.EventType eventType = rowChage.getEventType();
            if (rowChage.getIsDdl()) {
                log.info(
                        "binlog: {}:{}, table: {}.{}, eventType: {}, ddlSql: {}",
                        entry.getHeader().getLogfileName(),
                        entry.getHeader().getLogfileOffset(),
                        entry.getHeader().getSchemaName(),
                        entry.getHeader().getTableName(),
                        eventType,
                        rowChage.getSql());
            } else {
                log.info(
                        "binlog: {}:{}, table: {}.{}, eventType: {}",
                        entry.getHeader().getLogfileName(),
                        entry.getHeader().getLogfileOffset(),
                        entry.getHeader().getSchemaName(),
                        entry.getHeader().getTableName(),
                        eventType);
            }

            // 获取RowChange对象里的每一行数据，打印出来
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                log.info("eventType:{}", eventType);

                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getAfterColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    printColumn(rowData.getBeforeColumnsList());
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + ",updated=" + column.getUpdated());
        }
    }

    @PostConstruct
    public void connect() {
        connector.connect();
        connector.subscribe(); // 监听所有的表结构
        connector.rollback();
    }

    @Async
    @Scheduled(
            initialDelayString = "${canal.scheduled.initialDelay:2000}",
            fixedDelayString = "${canal.scheduled.fixedDelay:2000}")
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

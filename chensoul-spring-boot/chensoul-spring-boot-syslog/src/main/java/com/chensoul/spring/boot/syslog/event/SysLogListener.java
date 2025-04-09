package com.chensoul.spring.boot.syslog.event;

import com.chensoul.core.lambda.function.CheckedConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener {

	private final CheckedConsumer<SysLogInfo> consumer;

	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveLog(SysLogEvent sysLogEvent) {
		try {
			consumer.accept(sysLogEvent.getSource());
		}
		catch (Throwable e) {
			log.error("保存日志失败", e);
		}
	}

}

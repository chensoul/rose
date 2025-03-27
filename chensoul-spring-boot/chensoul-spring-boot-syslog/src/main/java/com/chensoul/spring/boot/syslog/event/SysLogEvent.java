package com.chensoul.spring.boot.syslog.event;

import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件
 */
public class SysLogEvent extends ApplicationEvent {
	public SysLogEvent(SysLogInfo sysLogInfo) {
		super(sysLogInfo);
	}

	public SysLogInfo getSource() {
		return (SysLogInfo) super.getSource();
	}
}

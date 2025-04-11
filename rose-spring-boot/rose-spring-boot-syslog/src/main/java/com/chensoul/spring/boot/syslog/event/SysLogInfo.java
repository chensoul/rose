package com.chensoul.spring.boot.syslog.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLogInfo implements Serializable {

	private static final long serialVersionUID = 1129753896999673095L;

	private String name;

	private String serverIp;

	private String clientIp;

	private String userAgent;

	private String requestUrl;

	private String requestParams;

	private String requestMethod;

	private Long costTime;

	private boolean success;

	private String exception;

	private String traceId;

	private String createdBy;

	private LocalDateTime createTime;

	private String tenantId;
}

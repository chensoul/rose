package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
public class Log implements Serializable {

	private static final long serialVersionUID = 1129753896999673095L;

	private String name;

	private String serverIp;

	private String remoteIp;

	private String remoteLocation;

	private String userAgent;

	private String requestUrl;

	private String requestParams;

	private String requestMethod;

	private String responseResult;

	private Long costTime;

	private boolean success;

	private String exception;

	private String traceId;

	@TableField(fill = FieldFill.INSERT)
	private String tenantId;

	private String createdBy;

	private LocalDateTime createTime;

}

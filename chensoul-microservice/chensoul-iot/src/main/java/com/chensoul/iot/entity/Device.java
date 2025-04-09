package com.chensoul.iot.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品Topic对象 product_topic
 */
@Data
public class Device {

	private static final long serialVersionUID = 1L;

	private Long productId;

	private String code;

	private String name;

	private String nodeType;

	private Long groupId;

	/**
	 * UNACTIVE
	 */
	private String status;

	private LocalDateTime activeTime;

	private LocalDateTime lastOnlineTime;

	private LocalDateTime lastOfflineTime;

}

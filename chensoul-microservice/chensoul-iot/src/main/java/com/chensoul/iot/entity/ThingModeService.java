package com.chensoul.iot.entity;

import lombok.Data;

/**
 * 产品物模型服务对象 product_model_service
 */
@Data
public class ThingModeService {

	private static final long serialVersionUID = 1L;

	private Long productId;

	private String identifier;

	private String name;

	/**
	 * async
	 */
	private String callType;

	private boolean required;

	private String method;

	private String inputData;

	private String outputData;

	private String description;

}

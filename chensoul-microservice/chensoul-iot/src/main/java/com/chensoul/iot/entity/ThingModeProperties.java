package com.chensoul.iot.entity;

import lombok.Data;

/**
 * 产品物模型属性对象 product_model_attr
 */
@Data
public class ThingModeProperties {

	private static final long serialVersionUID = 1L;

	private Long productId;

	private String name;

	private String identifier;

	//r、rw
	private String accessMode;

	private boolean required;

	/**
	 * {
	 * "type": "double",
	 * "specs": {
	 * "min": "1",
	 * "max": "100",
	 * "unit": "L/min",
	 * "unitName": "升每分钟",
	 * "step": "1"
	 * }
	 * }
	 * <p>
	 * {
	 * "type": "enum",
	 * "specs": {
	 * "1": "a",
	 * "2": "b"
	 * }
	 * }
	 * <p>
	 * {
	 * "type": "bool",
	 * "specs": {
	 * "0": "f",
	 * "1": "y"
	 * }
	 * }
	 * <p>
	 * {
	 * "type": "array",
	 * "specs": {
	 * "size": "10",
	 * "item": {
	 * "type": "int"
	 * }
	 * }
	 */
	private String dataType;

	private String description;
}

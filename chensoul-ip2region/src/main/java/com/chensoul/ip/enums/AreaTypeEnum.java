package com.chensoul.ip.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 区域类型枚举
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@AllArgsConstructor
@Getter
public enum AreaTypeEnum {

	COUNTRY(1, "国家"), PROVINCE(2, "省份"), CITY(3, "城市"), DISTRICT(4, "地区"), // 县、镇、区等
	;

	private final int code;

	private final String message;

}

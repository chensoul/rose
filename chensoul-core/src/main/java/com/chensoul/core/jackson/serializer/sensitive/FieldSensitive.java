package com.chensoul.core.jackson.serializer.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.chensoul.core.jackson.serializer.StringSensitiveSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@JacksonAnnotationsInside
@JsonSerialize(using = StringSensitiveSerialize.class)
public @interface FieldSensitive {

	SensitiveType type() default SensitiveType.CUSTOM;

	/**
	 * @return 前置不需要打码的长度
	 */
	int prefixKeep() default 0;

	/**
	 * @return 后置不需要打码的长度
	 */
	int suffixKeep() default 0;

	/**
	 * 用什么打码
	 * @return String
	 */
	String mask() default "*";

	/**
	 * 是否禁用脱敏
	 * <p>
	 * 支持 Spring EL 表达式，如果返回 true 则跳过脱敏
	 */
	String disabled() default "";

}

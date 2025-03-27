package com.chensoul.core.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举校验注解
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumCheckValidator.class})
public @interface EnumCheck {

	// 校验出错时默认返回的消息
	String message() default "枚举数据异常";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String propertyKey() default "code";

	/**
	 * 允许的枚举类型
	 *
	 * @return
	 */
	Class<?> enumClass();

	/**
	 * 同一个元素上指定多个该注解时使用
	 */
	@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		EnumCheck[] value();
	}
}

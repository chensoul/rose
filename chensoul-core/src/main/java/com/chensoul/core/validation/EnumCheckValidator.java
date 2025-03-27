package com.chensoul.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 枚举校验器
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class EnumCheckValidator implements ConstraintValidator<InEnum, Integer> {
	private List<?> values;

	@Override
	public void initialize(InEnum annotation) {
		Enum<?>[] values = annotation.value().getEnumConstants();
		if (values.length == 0) {
			this.values = Collections.emptyList();
		} else {
			this.values = Arrays.asList(values[0]);
		}
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		// 校验通过
		if (values.contains(value)) {
			return true;
		}
		// 校验不通过，自定义提示语句
		context.disableDefaultConstraintViolation(); // 禁用默认的 message 的值
		context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()
			.replaceAll("\\{value}", values.toString())).addConstraintViolation(); // 重新添加错误提示语句
		return false;
	}

}

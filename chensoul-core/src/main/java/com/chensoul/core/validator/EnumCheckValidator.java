package com.chensoul.core.validator;

import org.apache.commons.lang3.reflect.FieldUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 枚举校验器
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class EnumCheckValidator implements ConstraintValidator<EnumCheck, Integer> {

	private Class<?> enumClass;

	private String propertyKey;

	@Override
	public void initialize(EnumCheck constraintAnnotation) {
		enumClass = constraintAnnotation.enumClass();
		propertyKey = constraintAnnotation.propertyKey();
	}

	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {
		if (null == value) {
			return Boolean.TRUE;
		}

		AtomicBoolean isValid = new AtomicBoolean(false);
		for (Object enumConstant : enumClass.getEnumConstants()) {
			for (Field field : FieldUtils.getAllFields(enumClass)) {
				if (field.getName().equals(propertyKey)) {
					try {
						if ((!Modifier.isPublic(field.getModifiers()) ||
							!Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
							Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
							field.setAccessible(true);
						}
						if (field.get(enumConstant).equals(value)) {
							isValid.set(true);
							break;
						}
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return isValid.get();
	}
}

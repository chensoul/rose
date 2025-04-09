package com.chensoul.mybatis.functional;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.chensoul.core.exception.BusinessException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class BaseEntityOperation implements EntityOperation {

	static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	public <T> void doValidate(T t, Class<? extends Default> group) {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t, group, Default.class);
		if (ObjectUtils.isNotEmpty(constraintViolations)) {
			List<String> message = constraintViolations.stream()
				.map(cv -> cv.getMessage())
				.collect(Collectors.toList());
			throw new BusinessException(message.get(0));
		}
	}

}

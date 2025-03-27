package com.chensoul.core.validator;

import com.chensoul.core.exception.BusinessException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class Validators {
	public static final Pattern PROPERTY_PATTERN = Pattern.compile("^[\\p{L}0-9_-]+$"); // Unicode letters, numbers, '_' and '-' allowed

	public static void checkNotBlank(String val, String errorMessage) {
		if (StringUtils.isBlank(val)) {
			throw new BusinessException(errorMessage);
		}
	}

	public static void checkNotBlank(String val, Function<String, String> errorMessageFunction) {
		if (StringUtils.isBlank(val)) {
			throw new BusinessException(errorMessageFunction.apply(val));
		}
	}

	public static void checkPositiveNumber(long val, String errorMessage) {
		if (val <= 0) {
			throw new BusinessException(errorMessage);
		}
	}

	public <T> T checkNotNull(T reference) {
		return checkNotNull(reference, "请求的记录不存在");
	}

	public <T> T checkNotNull(T reference, String notFoundMessage) {
		if (reference == null) {
			throw new BusinessException(notFoundMessage);
		}
		return reference;
	}

	public <T> T checkNotNull(Optional<T> reference) {
		return checkNotNull(reference, "请求的记录不存在");
	}

	public <T> T checkNotNull(Optional<T> reference, String notFoundMessage) {
		if (reference.isPresent()) {
			return reference.get();
		} else {
			throw new BusinessException(notFoundMessage);
		}
	}

	public static boolean isValidProperty(String key) {
		return StringUtils.isEmpty(key) || PROPERTY_PATTERN.matcher(key).matches();
	}
}

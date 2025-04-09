package com.chensoul.core.exception;

import com.chensoul.core.domain.CodeNameAware;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 业务异常，必须提供code码，便于统一维护
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
public class BusinessException extends RuntimeException {

	private static final ResultCode DEFAULT = ResultCode.INTERNAL_ERROR;

	private final Integer code;

	private Serializable data;

	public BusinessException() {
		this(DEFAULT);
	}

	public BusinessException(Integer code, String message) {
		this(code, message, null);
	}

	public BusinessException(String message) {
		this(DEFAULT.getCode(), message, null);
	}

	public BusinessException(CodeNameAware resultCode) {
		this(resultCode.getCode(), resultCode.getName(), null);
	}

	public BusinessException(CodeNameAware resultCode, Serializable data) {
		this(resultCode.getCode(), resultCode.getName(), data);
	}

	public BusinessException(Integer code, String message, Serializable data) {
		super(message);
		this.code = code;
		this.data = data;
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
		this.code = DEFAULT.getCode();
	}

	public BusinessException(String message, Serializable data) {
		super(message);
		this.code = DEFAULT.getCode();
		this.data = data;
	}

	@Override
	public String getMessage() {
		return StringUtils.isBlank(super.getMessage()) ? DEFAULT.getName() : super.getMessage();
	}

}

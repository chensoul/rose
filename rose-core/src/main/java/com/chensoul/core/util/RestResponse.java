package com.chensoul.core.util;

import com.chensoul.core.exception.ResultCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 统一封装 Restful 接口返回信息
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RestResponse<T> implements Serializable {
	private int code;
	private String message;
	private T data;

	public static RestResponse<Void> ok() {
		return ok(null);
	}

	public static <T> RestResponse<T> ok(T data) {
		return ok(data, ResultCode.SUCCESS.getName());
	}

	public static <T> RestResponse<T> ok(T data, String message) {
		return build(data, ResultCode.SUCCESS.getCode(), message);
	}

	public static <T> RestResponse<T> error() {
		return error(null, ResultCode.INTERNAL_ERROR.getName());
	}

	public static <T> RestResponse<T> error(String message) {
		return error(null, message);
	}

	public static <T> RestResponse<T> error(T data) {
		return error(data, ResultCode.INTERNAL_ERROR.getName());
	}

	public static <T> RestResponse<T> error(T data, String message) {
		return build(data, ResultCode.INTERNAL_ERROR.getCode(), message);
	}

	public static <T> RestResponse<T> build(T data, int code, String message) {
		RestResponse<T> restResponse = new RestResponse<>();
		restResponse.code = code;
		restResponse.message = message;
		restResponse.data = data;
		return restResponse;
	}

	@JsonIgnore
	public Boolean isSuccess() {
		return (this.code == ResultCode.SUCCESS.getCode());
	}

	@JsonIgnore
	public Map<String, Object> toMap() {
		return Maps.of("code", code, "message", message, "data", data);
	}
}

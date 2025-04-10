package com.chensoul.core.util;

import com.chensoul.core.exception.ResultCode;
import groovy.transform.ToString;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 统一封装 Restful 接口返回信息
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
@ToString
public class RestResponse<T> implements Serializable {

	public static final Predicate<RestResponse<?>> CODE_SUCCESS = r -> ResultCode.SUCCESS.getCode() == r.getCode();

	private int code;

	private String message;

	private T data;

	private RestResponse() {
	}

	private RestResponse(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static RestResponse<Void> ok() {
		return ok(null);
	}

	public static <T> RestResponse<T> ok(T data) {
		return new RestResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getName(), data);
	}

	public static <T> RestResponse<T> ok(T data, String message) {
		return new RestResponse<>(ResultCode.SUCCESS.getCode(), message, data);
	}

	public static <T> RestResponse<T> error(String message) {
		return error(message, null);
	}

	public static <T> RestResponse<T> error(T data) {
		return error(ResultCode.INTERNAL_ERROR.getName(), data);
	}

	public static <T> RestResponse<T> error(String message, T data) {
		return error(ResultCode.INTERNAL_ERROR.getCode(), message, data);
	}

	public static <T> RestResponse<T> error(int code, String message) {
		return build(code, message, null);
	}

	public static <T> RestResponse<T> error(int code, String message, T data) {
		return build(code, message, data);
	}

	public static <T> RestResponse<T> build(int code, String message, T data) {
		return new RestResponse<>(code, message, data);
	}

	public Boolean isSuccess() {
		return (this.code == ResultCode.SUCCESS.getCode());
	}

	public Optional<T> ofData() {
		return Optional.ofNullable(data);
	}

	public Optional<T> ofDataIf(Predicate<? super RestResponse<?>> predicate) {
		return predicate.test(this) ? ofData() : Optional.empty();
	}

	public boolean codeEquals(int value) {
		return this.getCode() == value;
	}

	public boolean codeNotEquals(int value) {
		return !codeEquals(value);
	}

	public <Ex extends Exception> RestResponse<T> assertCode(int expect,
															 Function<? super RestResponse<T>, ? extends Ex> func) throws Ex {
		if (codeNotEquals(expect)) {
			throw func.apply(this);
		}
		return this;
	}

	public <Ex extends Exception> RestResponse<T> assertSuccess(Function<? super RestResponse<T>, ? extends Ex> func)
		throws Ex {
		return assertCode(ResultCode.SUCCESS.getCode(), func);
	}

	public <Ex extends Exception> RestResponse<T> assertDataNotNull(
		Function<? super RestResponse<T>, ? extends Ex> func) throws Ex {
		if (Objects.isNull(this.getData())) {
			throw func.apply(this);
		}
		return this;
	}

	public <Ex extends Exception> RestResponse<T> assertDataNotEmpty(
		Function<? super RestResponse<T>, ? extends Ex> func) throws Ex {
		if (ObjectUtils.isEmpty(this.getData())) {
			throw func.apply(this);
		}
		return this;
	}

	public <U> RestResponse<U> map(Function<? super T, ? extends U> mapper) {
		return RestResponse.build(this.getCode(), this.getMessage(), mapper.apply(this.getData()));
	}

	public void useData(Consumer<? super T> consumer) {
		consumer.accept(this.getData());
	}

	public void useDataOnCode(Consumer<? super T> consumer, int... codes) {
		useDataIf(o -> Arrays.stream(codes).filter(c -> this.getCode() == c).findFirst().isPresent(), consumer);
	}

	public void useDataIfSuccess(Consumer<? super T> consumer) {
		useDataIf(CODE_SUCCESS, consumer);
	}

	public void useDataIf(Predicate<? super RestResponse<T>> predicate, Consumer<? super T> consumer) {
		if (predicate.test(this)) {
			consumer.accept(this.getData());
		}
	}

}

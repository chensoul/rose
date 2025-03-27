package com.chensoul.core.ratelimit.handler;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RejectedHandler;

public class DefaultValueRejectedHandler<T> implements RejectedHandler<T> {
	private final T defaultValue;

	public DefaultValueRejectedHandler(T defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public T handleRejection(RateLimitContext context) {
		return defaultValue;
	}
}

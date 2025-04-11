package com.chensoul.core.ratelimit.handler;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RejectedHandler;

public class FallbackRejectedHandler<T> implements RejectedHandler<T> {
	private final FallbackFunction<T> fallbackFunction;

	public FallbackRejectedHandler(FallbackFunction<T> fallbackFunction) {
		this.fallbackFunction = fallbackFunction;
	}

	@Override
	public T handleRejection(RateLimitContext context) {
		return fallbackFunction.apply(context);
	}

	@FunctionalInterface
	public interface FallbackFunction<T> {
		T apply(RateLimitContext context);
	}
}

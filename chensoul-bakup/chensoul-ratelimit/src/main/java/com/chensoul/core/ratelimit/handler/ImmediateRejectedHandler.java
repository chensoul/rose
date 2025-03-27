package com.chensoul.core.ratelimit.handler;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RateLimitException;
import com.chensoul.core.ratelimit.RejectedHandler;

public class ImmediateRejectedHandler<T> implements RejectedHandler<T> {
	@Override
	public T handleRejection(RateLimitContext rateLimitContext) {
		throw new RateLimitException("Too many requests for " + rateLimitContext);
	}
}

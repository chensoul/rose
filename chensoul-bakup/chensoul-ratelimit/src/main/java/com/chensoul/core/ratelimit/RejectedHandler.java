package com.chensoul.core.ratelimit;

@FunctionalInterface
public interface RejectedHandler<T> {
	T handleRejection(RateLimitContext context); // 处理被限流的请求
}

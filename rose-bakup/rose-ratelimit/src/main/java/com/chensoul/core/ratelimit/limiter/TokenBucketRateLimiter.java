package com.chensoul.core.ratelimit.limiter;

import com.chensoul.core.ratelimit.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

// 令牌桶算法
public class TokenBucketRateLimiter implements RateLimiter {
	private final ConcurrentHashMap<String, com.google.common.util.concurrent.RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
	private final Integer limit; // 限流阈值
	private final long windowSizeInMillis; // 时间窗口大小

	public TokenBucketRateLimiter(Integer limit, long windowSizeInMillis) {
		this.limit = limit;
		this.windowSizeInMillis = windowSizeInMillis;
	}

	@Override
	public boolean tryAcquire(String key) {
		com.google.common.util.concurrent.RateLimiter rateLimiter = rateLimiterMap.computeIfAbsent(key, k -> com.google.common.util.concurrent.RateLimiter.create(limit, windowSizeInMillis, TimeUnit.MILLISECONDS));
		return rateLimiter.tryAcquire();
	}
}



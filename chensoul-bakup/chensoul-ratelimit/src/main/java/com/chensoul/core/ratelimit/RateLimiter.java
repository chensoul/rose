package com.chensoul.core.ratelimit;

public interface RateLimiter {
	boolean tryAcquire(String key); // 尝试获取令牌
}

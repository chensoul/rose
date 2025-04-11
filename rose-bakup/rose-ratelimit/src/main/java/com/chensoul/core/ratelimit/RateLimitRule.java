package com.chensoul.core.ratelimit;

public interface RateLimitRule {
	boolean allowRequest(RateLimitContext context); // 判断是否允许请求
}

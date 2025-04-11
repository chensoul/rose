package com.chensoul.core.ratelimit.rule;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RateLimitRule;
import com.chensoul.core.ratelimit.limiter.TokenBucketRateLimiter;

import java.util.HashMap;
import java.util.Map;

public class RequestResourceRateLimitRule implements RateLimitRule {
	private final Map<String, Integer> resourceLimits; // 接口 -> 限流阈值

	public RequestResourceRateLimitRule() {
		resourceLimits = new HashMap<>();
		resourceLimits.put("order", 1000); // 核心业务接口：每秒1000次
		resourceLimits.put("userInfo", 500); // 非核心业务接口：每秒500次
		resourceLimits.put("admin", 100);   // 管理类接口：每秒100次
	}

	@Override
	public boolean allowRequest(RateLimitContext context) {
		String apiName = context.getRequestResource();
		int limit = resourceLimits.getOrDefault(apiName, 100); // 默认管理类接口
		// 实现限流逻辑（如令牌桶算法）
		TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(limit, 1000);
		return tokenBucketRateLimiter.tryAcquire(context.getRequestResource());
	}
}

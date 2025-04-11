package com.chensoul.core.ratelimit.rule;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RateLimitRule;
import com.chensoul.core.ratelimit.limiter.FixedWindowRateLimiter;

import java.util.HashMap;
import java.util.Map;

public class UserLevelRateLimitRule implements RateLimitRule {
	private final Map<String, FixedWindowRateLimiter> userLevelLimits; // 用户等级 -> 限流阈值
	private final int defaultWindowSizeInMillis = 2 * 1000;
	private final FixedWindowRateLimiter defaultRateLimiter = new FixedWindowRateLimiter(1, defaultWindowSizeInMillis);

	public UserLevelRateLimitRule() {
		userLevelLimits = new HashMap<>();
		userLevelLimits.put("normal", new FixedWindowRateLimiter(1, defaultWindowSizeInMillis));
		userLevelLimits.put("silver", new FixedWindowRateLimiter(30, defaultWindowSizeInMillis));
		userLevelLimits.put("gold", new FixedWindowRateLimiter(50, defaultWindowSizeInMillis));
		userLevelLimits.put("vip", new FixedWindowRateLimiter(100, defaultWindowSizeInMillis));
	}

	@Override
	public boolean allowRequest(RateLimitContext context) {
		String userLevel = context.getUserLevel();
		FixedWindowRateLimiter userRateLimiter = userLevelLimits.getOrDefault(userLevel, defaultRateLimiter);
		return userRateLimiter.tryAcquire(context.getUserLevel());
	}
}

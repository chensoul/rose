package com.chensoul.core.ratelimit.rule;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RateLimitRule;
import com.chensoul.core.ratelimit.limiter.FixedWindowRateLimiter;

public class IpRateLimitRule implements RateLimitRule {
	private static final int defaultWindowSizeInMillis = 2 * 1000;
	private FixedWindowRateLimiter fixedWindowRateLimiter = null;

	public IpRateLimitRule(int limit, int windowSizeInMillis) {
		fixedWindowRateLimiter = new FixedWindowRateLimiter(limit, windowSizeInMillis);
	}

	public IpRateLimitRule(int limit) {
		this(limit, defaultWindowSizeInMillis);
	}

	@Override
	public boolean allowRequest(RateLimitContext context) {
		return fixedWindowRateLimiter.tryAcquire(context.getIp());
	}
}

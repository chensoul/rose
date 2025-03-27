package com.chensoul.core.ratelimit.limiter;

import com.chensoul.core.ratelimit.RateLimiter;

public class SlidingWindowRateLimiter implements RateLimiter {

	@Override
	public boolean tryAcquire(String key) {
		return false;
	}
}

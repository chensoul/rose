package com.chensoul.core.ratelimit.rule;

import com.chensoul.core.ratelimit.RateLimitContext;
import com.chensoul.core.ratelimit.RateLimitRule;

import java.util.List;

public class ComposedRateLimitRule implements RateLimitRule {
	List<RateLimitRule> rules = null;

	public ComposedRateLimitRule(List<RateLimitRule> rules) {
		this.rules = rules;
	}

	@Override
	public boolean allowRequest(RateLimitContext context) {
		return true;
	}
}

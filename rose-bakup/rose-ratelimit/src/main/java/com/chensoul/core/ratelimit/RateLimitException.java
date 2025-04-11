package com.chensoul.core.ratelimit;

public class RateLimitException extends RuntimeException {
	public RateLimitException(String message) {
		super(message);
	}
}

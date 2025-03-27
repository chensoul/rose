package com.chensoul.core.ratelimit;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RateLimitExecutor<T> {
	private final RateLimitRule rateLimitRule;
	private final RejectedHandler<T> rejectedHandler;

	public RateLimitExecutor(RateLimitRule rateLimitRule, RejectedHandler<T> rejectedHandler) {
		this.rateLimitRule = rateLimitRule;
		this.rejectedHandler = rejectedHandler;
	}

	public T execute(RateLimitContext context, Supplier<T> supplier) {
		if (rateLimitRule.allowRequest(context)) {
			return supplier.get(); // 允许通过，执行原始逻辑
		} else {
			return rejectedHandler.handleRejection(context); // 被限流，执行拒绝策略
		}
	}

	public void execute(RateLimitContext context, Consumer<T> consumer, T t) {
		if (rateLimitRule.allowRequest(context)) {
			consumer.accept(t); // 允许通过，执行原始逻辑
		} else {
			rejectedHandler.handleRejection(context); // 被限流，执行拒绝策略
		}
	}
}

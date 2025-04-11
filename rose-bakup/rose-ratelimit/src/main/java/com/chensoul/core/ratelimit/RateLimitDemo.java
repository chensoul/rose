package com.chensoul.core.ratelimit;

import com.chensoul.core.ratelimit.handler.DefaultValueRejectedHandler;
import com.chensoul.core.ratelimit.rule.UserLevelRateLimitRule;

public class RateLimitDemo {
	public static void main(String[] args) {
		// 创建限流规则
		RateLimitRule rule = new UserLevelRateLimitRule();

		// 创建拒绝策略
		RejectedHandler<String> rejectedHandler = new DefaultValueRejectedHandler<>("Rate limit exceeded");

		// 创建限流器
		RateLimitExecutor<String> rateLimitExecutor = new RateLimitExecutor<>(rule, rejectedHandler);

		// 创建限流上下文
		RateLimitContext context = new RateLimitContext();
		context.setUserLevel("normal"); // 普通用户

		// 模拟请求
		for (int i = 1; i <= 10; i++) {
			int finalI = i;
			String result = rateLimitExecutor.execute(context, () -> "Processed request " + finalI);
			System.out.println(result);
			try {
				Thread.sleep(300); // 模拟请求间隔
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

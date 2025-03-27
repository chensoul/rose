package com.chensoul.core.ratelimit;

import lombok.Data;

@Data
public class RateLimitContext {
	private String userId;    // 用户ID
	private String userLevel; // 用户等级
	private String requestResource;   // 请求资源
	private String requestMethod;   // 请求方法
	private String ip;        // IP地址
}

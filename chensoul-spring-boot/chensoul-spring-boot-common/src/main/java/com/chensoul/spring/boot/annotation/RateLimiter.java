package com.chensoul.spring.boot.annotation;

import com.chensoul.spring.boot.ratelimiter.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author canghe
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

	String RATE_LIMIT_KEY = "rate_limit:";

	/**
	 * 限流key
	 */
	public String key() default RATE_LIMIT_KEY;

	/**
	 * 限流时间,单位秒
	 */
	public int time() default 60;

	/**
	 * 限流次数
	 */
	public int count() default 100;

	/**
	 * 限流类型
	 */
	public LimitType limitType() default LimitType.DEFAULT;

}

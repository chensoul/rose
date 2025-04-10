package com.chensoul.spring.boot.aspect;

import com.chensoul.core.exception.TooManyRequestException;
import com.chensoul.core.util.NetUtils;
import com.chensoul.spring.boot.annotation.RateLimiter;
import com.chensoul.spring.boot.ratelimiter.LimitType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * 限流处理
 *
 * @author canghe
 */
@Aspect
@Slf4j
@Component
@ConditionalOnBean({RedisTemplate.class, RedisScript.class})
@RequiredArgsConstructor
public class RateLimiterAspect {

	private final RedisTemplate<String, Object> redisTemplate;

	private final RedisScript<Long> limitScript;

	@Before("@annotation(rateLimiter)")
	public void doBefore(JoinPoint point, RateLimiter rateLimiter) throws Throwable {
		int time = rateLimiter.time();
		int count = rateLimiter.count();

		String combineKey = getCombineKey(rateLimiter, point);
		List<String> keys = Collections.singletonList(combineKey);
		try {
			Long number = redisTemplate.execute(limitScript, keys, count, time);
			if (ObjectUtils.isEmpty(number) || number.intValue() > count) {
				throw new TooManyRequestException("访问过于频繁，请稍候再试");
			}
			log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), combineKey);
		} catch (TooManyRequestException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("服务器限流异常，请稍候再试");
		}
	}

	public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
		StringBuilder stringBuffer = new StringBuilder(rateLimiter.key());
		if (rateLimiter.limitType() == LimitType.IP) {
			stringBuffer.append(NetUtils.getLocalhostStr()).append("-");
		}
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Class<?> targetClass = method.getDeclaringClass();
		stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
		return stringBuffer.toString();
	}

}

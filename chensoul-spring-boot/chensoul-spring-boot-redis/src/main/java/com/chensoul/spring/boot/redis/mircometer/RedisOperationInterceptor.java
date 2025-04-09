package com.chensoul.spring.boot.redis.mircometer;

import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;

/**
 * Redis 操作拦截器
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public interface RedisOperationInterceptor extends Ordered {

	void before(Object wrapper, Object delegate, String methodName, Object[] args);

	void after(Object wrapper, Object delegate, String methodName, Object[] args, @Nullable Object result,
			@Nullable Throwable failure);

	default void afterReturning(Object wrapper, Object delegate, String methodName, Object[] args, Object result) {
		after(wrapper, delegate, methodName, args, result, null);
	}

	default void afterThrowing(Object wrapper, Object delegate, String methodName, Object[] args, Throwable failure) {
		after(wrapper, delegate, methodName, args, null, failure);
	}

	@Override
	default int getOrder() {
		return 0;
	}

	@Override
	boolean equals(Object object);

	@Override
	int hashCode();

}

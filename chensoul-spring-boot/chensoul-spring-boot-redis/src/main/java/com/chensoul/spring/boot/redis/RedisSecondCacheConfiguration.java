package com.chensoul.spring.boot.redis;

import com.chensoul.spring.boot.redis.secondcache.LocalCacheService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.TaskScheduler;

import java.util.concurrent.TimeUnit;

/**
 * Redis Configuration
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration
@ConditionalOnClass(Cache.class)
public class RedisSecondCacheConfiguration {
	@Bean
	public Cache<String, Object> localCache() {
		return Caffeine.newBuilder()
			.initialCapacity(100)
			.maximumSize(1000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();
	}

	@Bean
	@ConditionalOnBean({LettuceConnectionFactory.class, TaskScheduler.class})
	public LocalCacheService localCacheService(LettuceConnectionFactory connectionFactory, Cache cache, TaskScheduler taskScheduler) {
		return new LocalCacheService(connectionFactory, cache, taskScheduler);
	}
}

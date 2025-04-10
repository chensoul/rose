package com.chensoul.spring.boot.redis;

import com.chensoul.spring.boot.redis.support.TtlRedisCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@EnableCaching
@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheAutoConfiguration {

	private final RedisSerializer<Object> redisSerializer;

	private final CacheProperties cacheProperties;

	private final CacheManagerCustomizers cacheManagerCustomizers;

	@Bean
	public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory)
			.withStatisticsCollector(CacheStatisticsCollector.create());
		RedisCacheConfiguration cacheConfiguration = redisCacheConfiguration()
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

		TtlRedisCacheManager cacheManager = new TtlRedisCacheManager(redisCacheWriter, cacheConfiguration,
			this.cacheProperties.getCacheNames().toArray(new String[]{}));
		cacheManager.setTransactionAware(false);
		return this.cacheManagerCustomizers.customize(cacheManager);
	}

	@Bean
	@Primary
	public RedisCacheConfiguration redisCacheConfiguration() {
		log.info("Initializing RedisCacheConfiguration");

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

		CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}

		if (redisProperties.getKeyPrefix() != null) {
			config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
		}

		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}

		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}
		return config;
	}

}

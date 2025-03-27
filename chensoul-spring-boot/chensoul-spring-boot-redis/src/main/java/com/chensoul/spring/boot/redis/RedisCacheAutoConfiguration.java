package com.chensoul.spring.boot.redis;

import com.chensoul.spring.boot.redis.support.TimeoutRedisCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.chensoul.core.util.StringPool.COLON;

@Slf4j
@EnableCaching
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RedisCacheAutoConfiguration {
	private final RedisSerializer<Object> redisSerializer;
	private final CacheProperties cacheProperties;
	private final CacheManagerCustomizers cacheManagerCustomizers;

	@Bean
	public RedisCacheManager redisCacheManager(RedisTemplate<String, Object> redisTemplate,
											   RedisCacheConfiguration redisCacheConfiguration) {
		// 创建 RedisCacheWriter 对象
		RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
		RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
		// 创建 TenantRedisCacheManager 对象
		TimeoutRedisCacheManager cacheManager = new TimeoutRedisCacheManager(cacheWriter, redisCacheConfiguration);
		return this.cacheManagerCustomizers.customize(cacheManager);
	}

	@Bean
	@Primary
	public RedisCacheConfiguration redisCacheConfiguration() {
		log.info("Initializing RedisCacheConfiguration");

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
		// 设置使用 : 单冒号，而不是双 :: 冒号，避免 Redis Desktop Manager 多余空格
		config = config.computePrefixWith(cacheName -> {
			String keyPrefix = cacheProperties.getRedis().getKeyPrefix();
			if (StringUtils.hasText(keyPrefix)) {
				keyPrefix = keyPrefix.lastIndexOf(COLON) == -1 ? keyPrefix + COLON : keyPrefix;
				return keyPrefix + cacheName + COLON;
			}
			return cacheName + COLON;
		});


		CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
		config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
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

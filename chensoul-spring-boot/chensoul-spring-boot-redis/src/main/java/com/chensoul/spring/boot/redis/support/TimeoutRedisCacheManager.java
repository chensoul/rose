package com.chensoul.spring.boot.redis.support;

import com.chensoul.core.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 支持自定义过期时间的 {@link RedisCacheManager} 实现类
 * <p>
 * 在 {@link Cacheable#cacheNames()} 格式为 "key#120s" 时，# 后面的为过期时间
 */
@Slf4j
public class TimeoutRedisCacheManager extends RedisCacheManager {

	public TimeoutRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
		super(cacheWriter, defaultCacheConfiguration);
	}

	@Override
	protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {
		if (StringUtils.isBlank(name) || !name.contains(StringPool.HASH)) {
			return super.createRedisCache(name, cacheConfig);
		}
		String[] cacheArray = name.split(StringPool.HASH);
		if (cacheArray.length < 2) {
			return super.createRedisCache(name, cacheConfig);
		}
		String cacheName = cacheArray[0];
		if (cacheConfig != null) {
			Duration cacheAge = DurationStyle.detectAndParse(cacheArray[1], ChronoUnit.SECONDS);
			cacheConfig = cacheConfig.entryTtl(cacheAge);
		}
		return super.createRedisCache(cacheName, cacheConfig);
	}

}

package com.chensoul.spring.boot.redis.secondcache;

import com.github.benmanes.caffeine.cache.Cache;
import io.lettuce.core.support.caching.CacheAccessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Caffeine Cache Accessor
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@AllArgsConstructor
@Slf4j
public class CaffeineCacheAccessor implements CacheAccessor {

	private Cache cache;

	@Override
	public Object get(Object key) {
		log.info("caffeine get key: {}", key);
		return cache.getIfPresent(key);
	}

	@Override
	public void put(Object key, Object value) {
		log.info("caffeine put key: {}", key);

		cache.put(key, value);
	}

	@Override
	public void evict(Object key) {
		log.info("caffeine invalidate key: {}", key);

		cache.invalidate(key);
	}

}

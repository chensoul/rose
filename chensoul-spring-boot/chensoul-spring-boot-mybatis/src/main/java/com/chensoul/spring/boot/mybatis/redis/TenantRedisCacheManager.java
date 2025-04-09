package com.chensoul.spring.boot.mybatis.redis;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.chensoul.mybatis.tenant.util.TenantContextHolder;
import com.chensoul.spring.boot.redis.support.TimeoutRedisCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.util.Set;

import static com.chensoul.core.util.StringPool.COLON;

/**
 * 多租户的 {@link RedisCacheManager} 实现类
 * <p>
 * 操作指定 name 的 {@link Cache} 时，自动拼接租户后缀，格式为 name + ":" + tenantId + 后缀
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Slf4j
public class TenantRedisCacheManager extends TimeoutRedisCacheManager {

	private final Set<String> ignoredCaches;

	public TenantRedisCacheManager(Set<String> ignoredCaches, RedisCacheWriter cacheWriter,
			RedisCacheConfiguration defaultCacheConfiguration) {
		super(cacheWriter, defaultCacheConfiguration);
		this.ignoredCaches = ignoredCaches;
	}

	@Override
	public Cache getCache(String name) {
		// 如果开启多租户，则 name 拼接租户后缀
		if (!TenantContextHolder.isIgnored() && StringUtils.isNotBlank(TenantContextHolder.getTenantId())
				&& (CollectionUtils.isEmpty(ignoredCaches) || !ignoredCaches.contains(name))) {
			name = name + COLON + TenantContextHolder.getTenantId();
		}

		return super.getCache(name);
	}

}

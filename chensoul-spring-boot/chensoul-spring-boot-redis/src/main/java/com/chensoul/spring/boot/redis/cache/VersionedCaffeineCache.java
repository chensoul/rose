/**
 * Copyright © 2016-2025 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.spring.boot.redis.cache;

import com.chensoul.core.domain.HasVersion;
import com.chensoul.core.util.Pair;
import java.io.Serializable;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public abstract class VersionedCaffeineCache<K extends VersionedCacheKey, V extends Serializable & HasVersion>
        extends CaffeineTransactionalCache<K, V> implements VersionedCache<K, V> {

    public VersionedCaffeineCache(CacheManager cacheManager, String cacheName) {
        super(cacheManager, cacheName);
    }

    @Override
    public CacheValueWrapper<V> get(K key) {
        Pair<Long, V> versionValuePair = doGet(key);
        if (versionValuePair != null) {
            return SimpleCacheValueWrapper.wrap(versionValuePair.getSecond());
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        Long version = getVersion(value);
        if (version == null) {
            return;
        }
        doPut(key, value, version);
    }

    private void doPut(K key, V value, Long version) {
        lock.lock();
        try {
            Pair<Long, V> versionValuePair = doGet(key);
            if (versionValuePair == null || version > versionValuePair.getFirst()) {
                failAllTransactionsByKey(key);
                cache.put(key, wrapValue(value, version));
            }
        } finally {
            lock.unlock();
        }
    }

    private Pair<Long, V> doGet(K key) {
        Cache.ValueWrapper source = cache.get(key);
        return source == null ? null : (Pair<Long, V>) source.get();
    }

    @Override
    public void evict(K key) {
        lock.lock();
        try {
            failAllTransactionsByKey(key);
            cache.evict(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void evict(K key, Long version) {
        if (version == null) {
            return;
        }
        doPut(key, null, version);
    }

    @Override
    void doPutIfAbsent(K key, V value) {
        cache.putIfAbsent(key, wrapValue(value, getVersion(value)));
    }

    private Pair<Long, V> wrapValue(V value, Long version) {
        return Pair.of(version, value);
    }
}

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
package com.chensoul.rose.redis.cache;

import com.chensoul.rose.core.domain.HasVersion;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public interface VersionedCache<K extends VersionedCacheKey, V extends Serializable & HasVersion>
        extends TransactionalCache<K, V> {

    CacheValueWrapper<V> get(K key);

    default V get(K key, Supplier<V> supplier) {
        return get(key, supplier, true);
    }

    default V get(K key, Supplier<V> supplier, boolean putToCache) {
        return Optional.ofNullable(get(key)).map(CacheValueWrapper::get).orElseGet(() -> {
            V value = supplier.get();
            if (putToCache) {
                put(key, value);
            }
            return value;
        });
    }

    void put(K key, V value);

    void evict(K key);

    void evict(Collection<K> keys);

    void evict(K key, Long version);

    default Long getVersion(V value) {
        if (value == null) {
            return 0L;
        } else if (value.getVersion() != null) {
            return value.getVersion();
        } else {
            return null;
        }
    }
}

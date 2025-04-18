/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.redis.cache;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CaffeineCacheTransaction<K extends Serializable, V extends Serializable>
        implements CacheTransaction<K, V> {

    @Getter
    private final UUID id = UUID.randomUUID();

    private final CaffeineTransactionalCache<K, V> cache;

    @Getter
    private final List<K> keys;

    private final Map<K, V> pendingPuts = new LinkedHashMap<>();

    @Getter
    @Setter
    private boolean failed;

    @Override
    public void put(K key, V value) {
        pendingPuts.put(key, value);
    }

    @Override
    public boolean commit() {
        return cache.commit(id, pendingPuts);
    }

    @Override
    public void rollback() {
        cache.rollback(id);
    }
}

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

import com.chensoul.rose.core.jackson.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.data.redis.serializer.SerializationException;

public class TypedJsonRedisSerializer<K, V> implements RedisSerializer<K, V> {

    private final TypeReference<V> valueTypeRef;

    public TypedJsonRedisSerializer(TypeReference<V> valueTypeRef) {
        this.valueTypeRef = valueTypeRef;
    }

    @Override
    public byte[] serialize(V v) throws SerializationException {
        return JacksonUtils.writeValueAsBytes(v);
    }

    @Override
    public V deserialize(K key, byte[] bytes) throws SerializationException {
        return JacksonUtils.fromBytes(bytes, valueTypeRef);
    }
}

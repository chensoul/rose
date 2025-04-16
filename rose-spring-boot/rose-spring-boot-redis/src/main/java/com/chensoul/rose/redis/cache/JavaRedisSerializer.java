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

import org.springframework.data.redis.serializer.SerializationException;

public class JavaRedisSerializer<K, V> implements RedisSerializer<K, V> {

    final org.springframework.data.redis.serializer.RedisSerializer<Object> serializer =
            org.springframework.data.redis.serializer.RedisSerializer.java();

    @Override
    public byte[] serialize(V value) throws SerializationException {
        return serializer.serialize(value);
    }

    @Override
    public V deserialize(K key, byte[] bytes) throws SerializationException {
        return (V) serializer.deserialize(bytes);
    }
}

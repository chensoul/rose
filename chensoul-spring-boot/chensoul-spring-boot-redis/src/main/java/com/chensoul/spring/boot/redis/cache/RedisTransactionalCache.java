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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.NullValue;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.util.JedisClusterCRC16;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public abstract class RedisTransactionalCache<K extends Serializable, V extends Serializable>
		implements TransactionalCache<K, V> {

	static final byte[] BINARY_NULL_VALUE = org.springframework.data.redis.serializer.RedisSerializer.java()
		.serialize(NullValue.INSTANCE);
	static final JedisPool MOCK_POOL = new JedisPool(); // non-null pool required for
														// JedisConnection to trigger
														// closing jedis connection

	protected final Expiration evictExpiration;

	protected final Expiration cacheTtl;

	protected final boolean cacheEnabled;

	@Getter
	private final String cacheName;

	@Getter
	private final JedisConnectionFactory connectionFactory;

	private final org.springframework.data.redis.serializer.RedisSerializer<String> keySerializer = StringRedisSerializer.UTF_8;

	private final RedisSerializer<K, V> valueSerializer;

	public RedisTransactionalCache(String cacheName, CacheSpecProperties cacheSpecProperties,
			RedisConnectionFactory connectionFactory, RedisCacheConfiguration configuration,
			RedisSerializer<K, V> valueSerializer) {
		this.cacheName = cacheName;
		this.connectionFactory = (JedisConnectionFactory) connectionFactory;
		this.valueSerializer = valueSerializer;
		this.evictExpiration = Expiration.from(configuration.getEvictTtlInMs(), TimeUnit.MILLISECONDS);
		this.cacheTtl = Optional.ofNullable(cacheSpecProperties)
			.map(CacheSpecProperties::getSpecs)
			.map(specs -> specs.get(cacheName))
			.map(CacheSpecs::getTimeToLiveInMinutes)
			.filter(ttl -> !ttl.equals(0))
			.map(ttl -> Expiration.from(ttl, TimeUnit.MINUTES))
			.orElseGet(Expiration::persistent);
		this.cacheEnabled = Optional.ofNullable(cacheSpecProperties)
			.map(CacheSpecProperties::getSpecs)
			.map(x -> x.get(cacheName))
			.map(CacheSpecs::getMaxSize)
			.map(size -> size > 0)
			.orElse(false);
	}

	@Override
	public CacheValueWrapper<V> get(K key) {
		if (!cacheEnabled) {
			return null;
		}
		try (RedisConnection connection = connectionFactory.getConnection()) {
			byte[] rawValue = doGet(key, connection);
			if (rawValue == null || rawValue.length == 0) {
				return null;
			}
			else if (Arrays.equals(rawValue, BINARY_NULL_VALUE)) {
				return SimpleCacheValueWrapper.empty();
			}
			else {
				V value = valueSerializer.deserialize(key, rawValue);
				return SimpleCacheValueWrapper.wrap(value);
			}
		}
	}

	protected byte[] doGet(K key, RedisConnection connection) {
		return connection.stringCommands().get(getRawKey(key));
	}

	@Override
	public void put(K key, V value) {
		if (!cacheEnabled) {
			return;
		}
		try (RedisConnection connection = connectionFactory.getConnection()) {
			put(key, value, connection);
		}
	}

	public void put(K key, V value, RedisConnection connection) {
		put(connection, key, value, RedisStringCommands.SetOption.UPSERT);
	}

	@Override
	public void putIfAbsent(K key, V value) {
		if (!cacheEnabled) {
			return;
		}
		try (RedisConnection connection = connectionFactory.getConnection()) {
			put(connection, key, value, RedisStringCommands.SetOption.SET_IF_ABSENT);
		}
	}

	@Override
	public void evict(K key) {
		if (!cacheEnabled) {
			return;
		}
		try (RedisConnection connection = connectionFactory.getConnection()) {
			connection.keyCommands().del(getRawKey(key));
		}
	}

	@Override
	public void evict(Collection<K> keys) {
		if (!cacheEnabled) {
			return;
		}
		// Redis expects at least 1 key to delete. Otherwise - ERR wrong number of
		// arguments for 'del' command
		if (keys.isEmpty()) {
			return;
		}
		try (RedisConnection connection = connectionFactory.getConnection()) {
			connection.keyCommands().del(keys.stream().map(this::getRawKey).toArray(byte[][]::new));
		}
	}

	@Override
	public void evictOrPut(K key, V value) {
		if (!cacheEnabled) {
			return;
		}
		try (RedisConnection connection = connectionFactory.getConnection()) {
			byte[] rawKey = getRawKey(key);
			Long records = connection.keyCommands().del(rawKey);
			if (records == null || records == 0) {
				// We need to put the value in case of Redis, because evict will NOT
				// cancel concurrent transaction used to "get" the missing value from
				// cache.
				connection.stringCommands()
					.set(rawKey, getRawValue(value), evictExpiration, RedisStringCommands.SetOption.UPSERT);
			}
		}
	}

	@Override
	public CacheTransaction<K, V> newTransactionForKey(K key) {
		byte[][] rawKey = new byte[][] { getRawKey(key) };
		RedisConnection connection = watch(rawKey);
		return new RedisCacheTransaction<>(this, connection);
	}

	@Override
	public CacheTransaction<K, V> newTransactionForKeys(List<K> keys) {
		RedisConnection connection = watch(keys.stream().map(this::getRawKey).toArray(byte[][]::new));
		return new RedisCacheTransaction<>(this, connection);
	}

	@Override
	public <R> R getAndPutInTransaction(K key, Supplier<R> dbCall, Function<V, R> cacheValueToResult,
			Function<R, V> dbValueToCacheValue, boolean cacheNullValue) {
		if (!cacheEnabled) {
			return dbCall.get();
		}
		return TransactionalCache.super.getAndPutInTransaction(key, dbCall, cacheValueToResult, dbValueToCacheValue,
				cacheNullValue);
	}

	protected RedisConnection getConnection(byte[] rawKey) {
		if (!connectionFactory.isRedisClusterAware()) {
			return connectionFactory.getConnection();
		}
		RedisConnection connection = connectionFactory.getClusterConnection();

		int slotNum = JedisClusterCRC16.getSlot(rawKey);
		Jedis jedis = new Jedis(String
			.valueOf((((JedisClusterConnection) connection).getNativeConnection().getConnectionFromSlot(slotNum))));

		JedisConnection jedisConnection = new JedisConnection(jedis, MOCK_POOL, jedis.getDB());
		jedisConnection.setConvertPipelineAndTxResults(connectionFactory.getConvertPipelineAndTxResults());

		return jedisConnection;
	}

	protected RedisConnection watch(byte[][] rawKeysList) {
		RedisConnection connection = getConnection(rawKeysList[0]);
		try {
			connection.watch(rawKeysList);
			connection.multi();
		}
		catch (Exception e) {
			connection.close();
			throw e;
		}
		return connection;
	}

	protected byte[] getRawKey(K key) {
		String keyString = cacheName + key.toString();
		byte[] rawKey;
		try {
			rawKey = keySerializer.serialize(keyString);
		}
		catch (Exception e) {
			log.warn("Failed to serialize the cache key: {}", key, e);
			throw new RuntimeException(e);
		}
		if (rawKey == null) {
			log.warn("Failed to serialize the cache key: {}", key);
			throw new IllegalArgumentException("Failed to serialize the cache key!");
		}
		return rawKey;
	}

	protected byte[] getRawValue(V value) {
		if (value == null) {
			return BINARY_NULL_VALUE;
		}
		else {
			try {
				long startTime = System.nanoTime();
				byte[] bytes = valueSerializer.serialize(value);
				return bytes;
			}
			catch (Exception e) {
				log.warn("Failed to serialize the cache value: {}", value, e);
				throw new RuntimeException(e);
			}
		}
	}

	public void put(RedisConnection connection, K key, V value, RedisStringCommands.SetOption setOption) {
		if (!cacheEnabled) {
			return;
		}
		byte[] rawKey = getRawKey(key);
		put(connection, rawKey, value, setOption);
	}

	public void put(RedisConnection connection, byte[] rawKey, V value, RedisStringCommands.SetOption setOption) {
		byte[] rawValue = getRawValue(value);
		connection.stringCommands().set(rawKey, rawValue, this.cacheTtl, setOption);
	}

	protected void executeScript(RedisConnection connection, byte[] scriptSha, byte[] luaScript, ReturnType returnType,
			int numKeys, byte[]... keysAndArgs) {
		try {
			connection.scriptingCommands().evalSha(scriptSha, returnType, numKeys, keysAndArgs);
		}
		catch (InvalidDataAccessApiUsageException ignored) {
			log.debug("Loading LUA with expected SHA [{}], connection [{}]", new String(scriptSha),
					connection.getNativeConnection());
			String actualSha = connection.scriptingCommands().scriptLoad(luaScript);
			if (!Arrays.equals(scriptSha, StringRedisSerializer.UTF_8.serialize(actualSha))) {
				String message = String.format(
						"SHA for LUA script wrong! Expected [%s], but actual [%s], connection [%s]",
						new String(scriptSha), actualSha, connection.getNativeConnection());
				throw new IllegalStateException(message);
			}
			try {
				connection.scriptingCommands().evalSha(scriptSha, returnType, numKeys, keysAndArgs);
			}
			catch (InvalidDataAccessApiUsageException exception) {
				log.warn("Slowly executing eval instead of fast evalSha", exception);
				connection.scriptingCommands().eval(luaScript, returnType, numKeys, keysAndArgs);
			}
		}
	}

}

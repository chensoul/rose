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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
@EnableConfigurationProperties(CacheSpecProperties.class)
@ConditionalOnMissingBean(CaffeineCacheConfiguration.class)
@ConditionalOnProperty(prefix = "redis.connection", value = "type", havingValue = "cluster")
public class RedisClusterConfiguration extends RedisCacheConfiguration {

	@Value("${mq.cluster.nodes:}")
	private String clusterNodes;

	@Value("${mq.cluster.max-redirects:12}")
	private Integer maxRedirects;

	@Value("${mq.cluster.useDefaultPoolConfig:true}")
	private boolean useDefaultPoolConfig;

	@Value("${mq.password:}")
	private String password;

	@Value("${mq.ssl.enabled:false}")
	private boolean useSsl;

	public JedisConnectionFactory loadFactory() {
		org.springframework.data.redis.connection.RedisClusterConfiguration clusterConfiguration = new org.springframework.data.redis.connection.RedisClusterConfiguration();
		clusterConfiguration.setClusterNodes(getNodes(clusterNodes));
		clusterConfiguration.setMaxRedirects(maxRedirects);
		clusterConfiguration.setPassword(password);
		return new JedisConnectionFactory(clusterConfiguration, buildClientConfig());
	}

	private JedisClientConfiguration buildClientConfig() {
		JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
		if (!useDefaultPoolConfig) {
			jedisClientConfigurationBuilder
				.usePooling()
				.poolConfig(buildPoolConfig());
		}
		if (useSsl) {
			jedisClientConfigurationBuilder
				.useSsl()
				.sslSocketFactory(createSslSocketFactory());
		}
		return jedisClientConfigurationBuilder.build();
	}
}

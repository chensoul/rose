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
@ConditionalOnProperty(prefix = "redis.connection", value = "type", havingValue = "sentinel")
public class RedisSentinelConfiguration extends RedisCacheConfiguration {

    @Value("${mq.sentinel.master:}")
    private String master;

    @Value("${mq.sentinel.sentinels:}")
    private String sentinels;

    @Value("${mq.sentinel.password:}")
    private String sentinelPassword;

    @Value("${mq.sentinel.useDefaultPoolConfig:true}")
    private boolean useDefaultPoolConfig;

    @Value("${mq.db:}")
    private Integer database;

    @Value("${mq.ssl.enabled:false}")
    private boolean useSsl;

    @Value("${mq.password:}")
    private String password;

    public JedisConnectionFactory loadFactory() {
        org.springframework.data.redis.connection.RedisSentinelConfiguration redisSentinelConfiguration =
                new org.springframework.data.redis.connection.RedisSentinelConfiguration();
        redisSentinelConfiguration.setMaster(master);
        redisSentinelConfiguration.setSentinels(getNodes(sentinels));
        redisSentinelConfiguration.setSentinelPassword(sentinelPassword);
        redisSentinelConfiguration.setPassword(password);
        redisSentinelConfiguration.setDatabase(database);
        return new JedisConnectionFactory(redisSentinelConfiguration, buildClientConfig());
    }

    private JedisClientConfiguration buildClientConfig() {
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder =
                JedisClientConfiguration.builder();
        if (!useDefaultPoolConfig) {
            jedisClientConfigurationBuilder.usePooling().poolConfig(buildPoolConfig());
        }
        if (useSsl) {
            jedisClientConfigurationBuilder.useSsl().sslSocketFactory(createSslSocketFactory());
        }
        return jedisClientConfigurationBuilder.build();
    }
}

/*
 * Copyright © 2025 Chensoul, Inc. (ichensoul@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.redis.cache;

import java.time.Duration;
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
@ConditionalOnProperty(prefix = "redis.connection", value = "type", havingValue = "standalone")
public class RedisStandaloneConfiguration extends RedisCacheConfiguration {

    @Value("${mq.standalone.host:localhost}")
    private String host;

    @Value("${mq.standalone.port:6379}")
    private Integer port;

    @Value("${mq.standalone.clientName:standalone}")
    private String clientName;

    @Value("${mq.standalone.connectTimeout:30000}")
    private Long connectTimeout;

    @Value("${mq.standalone.readTimeout:60000}")
    private Long readTimeout;

    @Value("${mq.standalone.useDefaultClientConfig:true}")
    private boolean useDefaultClientConfig;

    @Value("${mq.standalone.usePoolConfig:false}")
    private boolean usePoolConfig;

    @Value("${mq.db:0}")
    private Integer db;

    @Value("${mq.password:}")
    private String password;

    @Value("${mq.ssl.enabled:false}")
    private boolean useSsl;

    public JedisConnectionFactory loadFactory() {
        org.springframework.data.redis.connection.RedisStandaloneConfiguration standaloneConfiguration =
                new org.springframework.data.redis.connection.RedisStandaloneConfiguration();
        standaloneConfiguration.setHostName(host);
        standaloneConfiguration.setPort(port);
        standaloneConfiguration.setDatabase(db);
        standaloneConfiguration.setPassword(password);
        return new JedisConnectionFactory(standaloneConfiguration, buildClientConfig());
    }

    private JedisClientConfiguration buildClientConfig() {
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder =
                JedisClientConfiguration.builder();
        if (!useDefaultClientConfig) {
            jedisClientConfigurationBuilder
                    .clientName(clientName)
                    .connectTimeout(Duration.ofMillis(connectTimeout))
                    .readTimeout(Duration.ofMillis(readTimeout));
        }
        if (useSsl) {
            jedisClientConfigurationBuilder.useSsl().sslSocketFactory(createSslSocketFactory());
        }
        if (usePoolConfig) {
            jedisClientConfigurationBuilder.usePooling().poolConfig(buildPoolConfig());
        }
        return jedisClientConfigurationBuilder.build();
    }
}

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
package com.chensoul.rose.config;

import com.chensoul.rose.core.spring.SpringContextHolder;
import com.chensoul.rose.core.spring.factory.YamlPropertySourceFactory;
import com.chensoul.rose.core.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Slf4j
@AutoConfiguration
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@PropertySource(value = "classpath:common-config.yml", factory = YamlPropertySourceFactory.class)
public class EnvironmentConfiguration {

    @Order
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        String appName = SpringContextHolder.getApplicationName();
        int localPort = event.getWebServer().getPort();
        String profiles = String.join(StringPool.COMMA, SpringContextHolder.getActiveProfiles());
        log.info("Application {} finish to start with port {} and {} profile", appName, localPort, profiles);
    }
}

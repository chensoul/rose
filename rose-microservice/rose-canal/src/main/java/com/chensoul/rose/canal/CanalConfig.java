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
package com.chensoul.rose.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
public class CanalConfig {

    @Value("${canal.server.ip}")
    private String canalServerIp;

    @Value("${canal.server.port}")
    private int canalServerPort;

    @Value("${canal.server.username:canal}")
    private String username;

    @Value("${canal.server.password:canal}")
    private String password;

    @Value("${canal.destinations:test}")
    private String destinations;

    @Bean("promotionConnector")
    public CanalConnector newSingleConnector() {
        return CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalServerIp, canalServerPort), destinations, username, password);
    }
}

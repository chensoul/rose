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

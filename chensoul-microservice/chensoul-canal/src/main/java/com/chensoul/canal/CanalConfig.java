package com.chensoul.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;

@Configuration
@EnableScheduling
@EnableAsync
public class CanalConfig {

	@Value("${canal.server.ip}")
	private String canalServerIp;

	@Value("${canal.server.port}")
	private int canalServerPort;

	@Value("${canal.server.username:}")
	private String username;

	@Value("${canal.server.password:}")
	private String password;

	@Value("${canal.destination:test}")
	private String destination;

	@Bean("promotionConnector")
	public CanalConnector newSingleConnector() {
		return CanalConnectors.newSingleConnector(new InetSocketAddress(canalServerIp,
			canalServerPort), destination, username, password);
	}
}

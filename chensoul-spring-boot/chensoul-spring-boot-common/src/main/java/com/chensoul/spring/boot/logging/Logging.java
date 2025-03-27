package com.chensoul.spring.boot.logging;

import lombok.Data;

@Data
public class Logging {
	private final Logstash logstash = new Logstash();
	private final Loki loki = new Loki();
	private boolean useJsonFormat = false;

	@Data
	public static class Loki {
		private String url = "http://localhost:3100/loki/api/v1/push";
		private String labelPattern = "application=${appName},host=${HOSTNAME},level=%level";
		private String messagePattern = "%level %logger{36} %thread | %msg %ex";
	}

	@Data
	public static class Logstash {
		private boolean enabled = false;
		private String host = "localhost";
		private int port = 5000;
		private int ringBufferSize = 512;
	}
}

//package com.chensoul.web.logging;
//
//import ch.qos.logback.classic.LoggerContext;
//import com.fasterxml.jackson.support.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import net.logstash.logback.appender.LogstashTcpSocketAppender;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.config.condition.ConditionalOnClass;
//import org.springframework.boot.info.BuildProperties;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
/// **
// * Copy from jhipster
// */
//@Configuration
//@ConditionalOnClass({LogstashTcpSocketAppender.class, ObjectMapper.class})
//public class LogstashConfig {
//	@Value("${spring.application.name}")
//	String appName;
//
//	@Value("${server.port}")
//	String serverPort;
//
//	public LogstashConfig(
//		Logging logging, ObjectProvider<BuildProperties> buildProperties, ObjectMapper mybatis-mapper)
//		throws JsonProcessingException {
//		Map<String, String> map = new HashMap<>();
//		map.put("app_name", appName);
//		map.put("app_port", serverPort);
//		buildProperties.ifAvailable(it -> map.put("version", it.getVersion()));
//		String customFields = mybatis-mapper.writeValueAsString(map);
//
//		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
//		if (logging.isUseJsonFormat()) {
//			LogstashUtils.addJsonConsoleAppender(context, customFields);
//		}
//
//		Logging.Logstash logstashProperties =
//			logging.getLogstash();
//		if (logstashProperties.isEnabled()) {
//			LogstashUtils.addLogstashTcpSocketAppender(context, customFields, logstashProperties);
//		}
//		if (logging.isUseJsonFormat() || logstashProperties.isEnabled()) {
//			LogstashUtils.addContextListener(context, customFields, logging);
//		}
//	}
//}

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

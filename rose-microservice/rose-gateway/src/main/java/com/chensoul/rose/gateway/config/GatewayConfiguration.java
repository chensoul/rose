package com.chensoul.rose.gateway.config;

import com.chensoul.rose.gateway.filter.RequestGlobalFilter;
import com.chensoul.rose.gateway.handler.GlobalExceptionHandler;
import com.chensoul.rose.gateway.handler.SentinelFallbackHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 网关配置
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

    @Bean
    public RequestGlobalFilter pigRequestGlobalFilter() {
        return new RequestGlobalFilter();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
        return new SentinelFallbackHandler();
    }
}

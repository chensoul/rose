package com.chensoul.rose.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    // @LoadBalanced
    // @ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue
    // = "true", matchIfMissing = true)
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // /**
    // * REST 客户端构建器（支持负载均衡）
    // *
    // * @return {@link RestClient.Builder }
    // */
    // @Bean
    // @LoadBalanced
    // @ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue
    // = "true", matchIfMissing = true)
    // RestClient.Builder restClientBuilder() {
    // return RestClient.builder();
    // }

}

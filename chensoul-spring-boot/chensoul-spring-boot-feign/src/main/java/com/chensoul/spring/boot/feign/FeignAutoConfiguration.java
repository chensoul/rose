package com.chensoul.spring.boot.feign;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.chensoul.spring.boot.feign.core.FeignInnerRequestInterceptor;
import com.chensoul.spring.boot.feign.core.FeignRequestCloseInterceptor;
import com.chensoul.spring.boot.feign.sentinel.ext.SentinelFeign;
import com.chensoul.spring.boot.feign.sentinel.handle.UrlBlockHandler;
import com.chensoul.spring.boot.feign.sentinel.parser.HeaderRequestOriginParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.AutoFeignClientsRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * sentinel 配置
 */
@Import(AutoFeignClientsRegistrar.class)
@AutoConfiguration(before = SentinelFeignAutoConfiguration.class)
public class FeignAutoConfiguration {

	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "feign.sentinel.enabled")
	public Feign.Builder feignSentinelBuilder() {
		return SentinelFeign.builder();
	}

	@Bean
	@ConditionalOnMissingBean
	public BlockExceptionHandler blockExceptionHandler(ObjectMapper objectMapper) {
		return new UrlBlockHandler(objectMapper);
	}

	@Bean
	@ConditionalOnMissingBean
	public RequestOriginParser requestOriginParser() {
		return new HeaderRequestOriginParser();
	}

	/**
	 * set connection close header
	 *
	 * @return RequestInterceptor
	 */
	@Bean
	public RequestInterceptor feignRequestCloseInterceptor() {
		return new FeignRequestCloseInterceptor();
	}

	/**
	 * pig feign 内部请求拦截器
	 *
	 * @return {@link RequestInterceptor }
	 */
	@Bean
	public RequestInterceptor feignInnerRequestInterceptor() {
		return new FeignInnerRequestInterceptor();
	}

}

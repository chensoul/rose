package com.chensoul.spring.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Slf4j
@Configuration
@ConditionalOnClass(RetryOperationsInterceptor.class)
public class RetryConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "configServerRetryInterceptor")
	public RetryOperationsInterceptor configServerRetryInterceptor() {
		log.info("Changing backOffOptions  to initial: {}, multiplier: {}, maxInterval: {}", 1000, 1.2, 5000);
		return RetryInterceptorBuilder
			.stateless()
			.backOffOptions(1000, 1.2, 5000)
			.maxAttempts(3)
			.build();
	}

}

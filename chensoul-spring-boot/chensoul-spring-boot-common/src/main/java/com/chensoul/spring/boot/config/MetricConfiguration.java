package com.chensoul.spring.boot.config;

import com.chensoul.core.util.NetUtils;
import com.chensoul.spring.boot.feign.MetricsInterceptor;
import com.chensoul.spring.boot.micrometer.AggravateMetricsEndpoint;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Configuration
@ConditionalOnClass(Timed.class)
@Import(MetricsInterceptor.class)
@AutoConfigureAfter(MetricsEndpointAutoConfiguration.class)
public class MetricConfiguration {

	@Bean
	@ConditionalOnAvailableEndpoint
	public AggravateMetricsEndpoint aggravateMetricsEndpoint(MeterRegistry meterRegistry) {
		log.info("Initializing AggravateMetricsEndpoint");

		return new AggravateMetricsEndpoint(meterRegistry);
	}

	@Bean
	@ConditionalOnClass(ProceedingJoinPoint.class)
	TimedAspect timedAspect(MeterRegistry registry) {
		return new TimedAspect(registry);
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> meterRegistryCustomizer() {
		return registry -> registry.config()
			.commonTags(Collections.singletonList(Tag.of("host", Objects.requireNonNull(NetUtils.getLocalhostStr()))));
	}

}

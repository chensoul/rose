package com.chensoul.rose.config;

import com.chensoul.rose.core.util.NetUtils;
import com.chensoul.rose.feign.MetricsInterceptor;
import com.chensoul.rose.micrometer.AggravateMetricsEndpoint;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import java.util.Collections;
import java.util.Objects;
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
                .commonTags(
                        Collections.singletonList(Tag.of("host", Objects.requireNonNull(NetUtils.getLocalhostStr()))));
    }
}

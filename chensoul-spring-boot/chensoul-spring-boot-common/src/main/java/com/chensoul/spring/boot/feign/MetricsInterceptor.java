package com.chensoul.spring.boot.feign;

import com.chensoul.core.util.StringPool;
import com.chensoul.spring.boot.micrometer.Micrometers;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.apache.commons.lang3.StringUtils;

/**
 * Feign 调用计数 Metrics
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public class MetricsInterceptor implements RequestInterceptor, MeterBinder {
	public static final String FEIGN_REQUEST = "feign.requests";
	public static final String FEIGN_REQUEST_ERROR = FEIGN_REQUEST + ".error";
	private static MeterRegistry meterRegistry;

	@Override
	public void apply(RequestTemplate template) { // FeignClient 子上下文调用
		// 异步执行
		Micrometers.async(() -> {
			// 方法统计
			String methodKey = template.methodMetadata().configKey();
			Counter counter = Counter.builder(FEIGN_REQUEST)
				.tags("method", StringUtils.substringBefore(methodKey, StringPool.LEFT_BRACKET))                   // Feign 调用方法（接口 + 方法） Tag
				.register(meterRegistry);
			counter.increment();
		});

	}

	@Override
	public void bindTo(MeterRegistry registry) { // Spring Boot 主上下文调用
		this.meterRegistry = registry;
	}
}

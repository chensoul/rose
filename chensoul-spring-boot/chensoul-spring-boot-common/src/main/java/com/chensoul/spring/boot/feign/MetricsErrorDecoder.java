package com.chensoul.spring.boot.feign;

import com.chensoul.core.util.StringPool;
import com.chensoul.spring.boot.micrometer.Micrometers;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.apache.commons.lang3.StringUtils;

import static com.chensoul.spring.boot.feign.MetricsInterceptor.FEIGN_REQUEST_ERROR;


public class MetricsErrorDecoder implements ErrorDecoder {
	private static MeterRegistry registry = new SimpleMeterRegistry();

	static {
		Counter.builder(FEIGN_REQUEST_ERROR).register(registry);
	}

	protected void metrics(String methodKey) {
		Micrometers.async(() -> Metrics.counter(FEIGN_REQUEST_ERROR, "method",
			StringUtils.substringBefore(methodKey, StringPool.LEFT_BRACKET)).increment());
	}

	@Override
	public Exception decode(String methodKey, Response response) {
		metrics(methodKey);

		FeignException exception = FeignException.errorStatus(methodKey, response);
		return exception;
	}
}

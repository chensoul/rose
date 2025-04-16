package com.chensoul.rose.feign;

import static com.chensoul.rose.feign.MetricsInterceptor.FEIGN_REQUEST_ERROR;

import com.chensoul.rose.core.util.StringPool;
import com.chensoul.rose.micrometer.Micrometers;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.apache.commons.lang3.StringUtils;

public class MetricsErrorDecoder implements ErrorDecoder {

    private static MeterRegistry registry = new SimpleMeterRegistry();

    static {
        Counter.builder(FEIGN_REQUEST_ERROR).register(registry);
    }

    protected void metrics(String methodKey) {
        Micrometers.async(() -> Metrics.counter(
                        FEIGN_REQUEST_ERROR, "method", StringUtils.substringBefore(methodKey, StringPool.LEFT_BRACKET))
                .increment());
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        metrics(methodKey);

        FeignException exception = FeignException.errorStatus(methodKey, response);
        return exception;
    }
}

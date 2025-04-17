/*
 * Copyright © 2025 Chensoul, Inc. (ichensoul@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

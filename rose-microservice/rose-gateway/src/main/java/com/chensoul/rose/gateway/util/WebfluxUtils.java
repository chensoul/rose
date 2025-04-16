package com.chensoul.rose.gateway.util;

import com.chensoul.rose.core.jackson.JacksonUtils;
import java.util.Objects;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

public final class WebfluxUtils {

    public static Mono<Void> writeResponse(ServerHttpResponse response, Object object) {
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        DataBuffer dataBuffer = response.bufferFactory().wrap(Objects.requireNonNull(JacksonUtils.toBytes(object)));
        return response.writeWith(Mono.just(dataBuffer));
    }
}

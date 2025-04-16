package com.chensoul.rose.feign.core;

import feign.RequestInterceptor;
import org.springframework.http.HttpHeaders;

/**
 * <p>
 * http connection close
 */
public class FeignRequestCloseInterceptor implements RequestInterceptor {

    /**
     * set connection close
     *
     * @param template
     */
    @Override
    public void apply(feign.RequestTemplate template) {
        template.header(HttpHeaders.CONNECTION, "close");
    }
}

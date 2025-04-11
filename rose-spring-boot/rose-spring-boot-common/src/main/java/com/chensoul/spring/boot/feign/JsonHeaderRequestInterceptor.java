package com.chensoul.spring.boot.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 4.0.5
 */
public class JsonHeaderRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-Type", "application/jackson;charset=UTF-8").header("Accept", "application/jackson");
    }
}

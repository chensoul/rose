package com.chensoul.rose.feign.core;

import com.chensoul.rose.core.SecurityConstants;
import com.chensoul.rose.feign.annotation.NoToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.lang.reflect.Method;
import org.springframework.core.Ordered;

public class FeignInnerRequestInterceptor implements RequestInterceptor, Ordered {

    /**
     * Called for every request. Add data using methods on the supplied
     * {@link RequestTemplate}.
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        Method method = template.methodMetadata().method();
        NoToken noToken = method.getAnnotation(NoToken.class);
        if (noToken != null) {
            template.header(SecurityConstants.FROM, SecurityConstants.FROM_IN);
        }
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}

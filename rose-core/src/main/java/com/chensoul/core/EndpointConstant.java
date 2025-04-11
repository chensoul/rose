package com.chensoul.core;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface EndpointConstant {

    String ALL = "/**";

    String CODE_URL = "/code";

    String OAUTH_ALL = "/oauth/**";

    String OAUTH_AUTHORIZE = "/oauth/authorize";

    String OAUTH_CHECK_TOKEN = "/oauth/check_token";

    String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";

    String OAUTH_TOKEN = "/oauth/token";

    String OAUTH_TOKEN_KEY = "/oauth/token_key";

    String OAUTH_ERROR = "/oauth/error";

    String ACTUATOR_ALL = "/actuator/**";

    String TOKEN_CONFIRM_ACCESS = "/token/confirm_access";

    String[] NON_TOKEN_BASED_AUTH_ENTRY_POINTS = new String[] {
        "/v2/api-docs",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/favicon.ico",
        ACTUATOR_ALL,
        "/error",
        CODE_URL,
        "/api/noauth/**",
        "/user/load"
    };
}

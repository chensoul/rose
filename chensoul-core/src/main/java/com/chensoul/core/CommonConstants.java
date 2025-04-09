package com.chensoul.core;

import org.springframework.core.Ordered;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface CommonConstants {

	String PROFILE_PROD = "prod";

	String PROFILE_NOT_PROD = "!" + PROFILE_PROD;

	String PROFILE_TEST = "test";

	String PROFILE_NOT_TEST = "!" + PROFILE_TEST;

	String BASE_PACKAGE = "com.chensoul";

	String PROJECT_NAME = "rose";

	int CORS_FILTER = Ordered.HIGHEST_PRECEDENCE;

	int CACHING_REQUEST_FILTER = CORS_FILTER + 1;

	int TRACE_FILTER = CORS_FILTER + 2;

	int XSS_FILTER = CORS_FILTER + 3;

	int TENANT_CONTEXT_FILTER = CORS_FILTER + 5;

	// Spring Security Filter 默认为 -100，可见
	// org.springframework.boot.autoconfigure.security.SecurityProperties 配置属性类
	int TENANT_SECURITY_FILTER = -99; // 需要保证在 Spring Security 过滤器后面

	String HEADER_TENANT_ID = "tenant-id";

	String REQUEST_START_TIME = "REQUEST-START-TIME";

}

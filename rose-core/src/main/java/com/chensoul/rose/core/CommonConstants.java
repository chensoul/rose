/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.core;

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

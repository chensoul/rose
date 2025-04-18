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
package com.chensoul.rose.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ConfigurationProperties(prefix = "security", ignoreUnknownFields = false)
public class SecurityProperties {

    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";

    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";

    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    public static final String[] DEFAULT_PATH_TO_SKIP = new String[] {
        TOKEN_REFRESH_ENTRY_POINT,
        FORM_BASED_LOGIN_ENTRY_POINT,
        "/api/noauth/**",
        "/error",
        "/actuator/**",
        "/api/upms/mail/oauth2/code"
    };

    private String baseUrl = TOKEN_BASED_AUTH_ENTRY_POINT;

    private String loginUrl = FORM_BASED_LOGIN_ENTRY_POINT;

    private String tokenRefreshUrl = TOKEN_REFRESH_ENTRY_POINT;

    private List<String> pathsToSkip = new ArrayList<>();

    private Long accessTokenExpireTime = 9000L;

    private Long refreshTokenExpireTime = 604800L;

    private JwtProperties jwt = new JwtProperties();

    public List<String> getPathsToSkip() {
        if (CollectionUtils.isEmpty(pathsToSkip)) {
            pathsToSkip.addAll(Arrays.asList(DEFAULT_PATH_TO_SKIP));
        }
        return pathsToSkip;
    }

    @Data
    public static class JwtProperties {

        private boolean enabled = false;

        private String tokenIssuer = "rose.chensoul.com";

        private String tokenSigningKey =
                "secret12345678901234567890123456789012345678901234567890123456789012345678901234567890";
    }
}

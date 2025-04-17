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
package com.chensoul.rose.security.rest.handler;

import com.chensoul.rose.core.spring.WebUtils;
import com.chensoul.rose.security.rest.mfa.MfaAuthenticationToken;
import com.chensoul.rose.security.support.TokenFactory;
import com.chensoul.rose.security.util.SecurityUser;
import com.chensoul.rose.security.util.TokenPair;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenFactory tokenFactory;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        TokenPair tokenPair;
        if (authentication instanceof MfaAuthenticationToken) {
            tokenPair = tokenFactory.createPreVerificationTokenPair(securityUser);
        } else {
            tokenPair = tokenFactory.createTokenPair(securityUser);
        }

        WebUtils.renderJson(HttpStatus.OK.value(), tokenPair);
    }
}

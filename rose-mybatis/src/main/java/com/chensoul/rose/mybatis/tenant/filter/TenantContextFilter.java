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
package com.chensoul.rose.mybatis.tenant.filter;

import static com.chensoul.rose.core.CommonConstants.HEADER_TENANT_ID;

import com.chensoul.rose.core.spring.WebUtils;
import com.chensoul.rose.mybatis.tenant.util.TenantContextHolder;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenantId = WebUtils.getValue(request, HEADER_TENANT_ID);
        if (tenantId != null) {
            log.info("Visit {} with tenantId {}", request.getRequestURI(), tenantId);
            TenantContextHolder.setTenantId(tenantId);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}

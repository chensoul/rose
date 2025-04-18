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
package com.chensoul.rose.feign.sentinel.handle;

import com.alibaba.csp.sentinel.Tracer;
import com.chensoul.rose.core.exception.BusinessException;
import com.chensoul.rose.core.util.RestResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 全局异常处理器结合 sentinel 全局异常处理器不能作用在 oauth server
 * </p>
 */
@Slf4j
@Order(10000)
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResponse<String> handleGlobalException(Exception e, HttpServletRequest request) {
        log.error("系统异常, {}, {}", request.getRequestURI(), e.getMessage(), e);

        // 业务异常交由 sentinel 记录
        Tracer.trace(e);
        return RestResponse.error("系统异常");
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<String> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        log.error("业务异常, {}, {}", request.getRequestURI(), exception.getMessage(), exception);
        return RestResponse.error(exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RestResponse<String> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String msg = SpringSecurityMessageSource.getAccessor()
                .getMessage("AbstractAccessDecisionManager.accessDenied", e.getMessage());
        log.warn("无权限访问, {}, {}", request.getRequestURI(), msg, e);
        return RestResponse.error(msg);
    }
}

package com.chensoul.rose.security.rest.handler;

import com.chensoul.rose.core.spring.WebUtils;
import com.chensoul.rose.core.util.RestResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
@RequiredArgsConstructor
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }

        log.error("Access denied: {}", e.getMessage(), e);
        WebUtils.renderJson(HttpStatus.FORBIDDEN.value(), RestResponse.error("无权限访问"));
    }
}

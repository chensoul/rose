package com.chensoul.security.rest.handler;

import com.chensoul.core.spring.WebUtils;
import com.chensoul.core.util.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException e) throws IOException, ServletException {
		if (response.isCommitted()) {
			return;
		}

		log.error("Authentication failure: {}", e.getMessage(), e);

		WebUtils.renderJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), RestResponse.error(e.getMessage()));
	}
}

package com.chensoul.security.rest.handler;

import com.chensoul.core.spring.WebUtils;
import com.chensoul.security.rest.mfa.MfaAuthenticationToken;
import com.chensoul.security.support.TokenFactory;
import com.chensoul.security.util.SecurityUser;
import com.chensoul.security.util.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final TokenFactory tokenFactory;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
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

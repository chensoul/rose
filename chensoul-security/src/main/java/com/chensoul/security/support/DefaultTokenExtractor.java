package com.chensoul.security.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.servlet.http.HttpServletRequest;

public class DefaultTokenExtractor implements TokenExtractor {

	@Override
	public String extract(HttpServletRequest request) {
		String header = request.getHeader(AUTHORIZATION);
		if (StringUtils.isNotBlank(header)) {
			if (header.length() < HEADER_PREFIX.length()) {
				throw new AuthenticationServiceException("Invalid authorization header size.");
			}
			header = header.substring(HEADER_PREFIX.length(), header.length());
		}
		else {
			header = request.getParameter(REQUEST_PREFIX);
		}

		if (StringUtils.isBlank(header)) {
			throw new AuthenticationServiceException("Authorization header cannot be blank!");
		}
		return header;
	}

}

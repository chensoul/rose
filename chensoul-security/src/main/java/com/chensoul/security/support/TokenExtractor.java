package com.chensoul.security.support;

import javax.servlet.http.HttpServletRequest;

public interface TokenExtractor {
	String HEADER_PREFIX = "Bearer ";
	String REQUEST_PREFIX = "accessToken";
	String AUTHORIZATION = "Authorization";

	String extract(HttpServletRequest request);
}

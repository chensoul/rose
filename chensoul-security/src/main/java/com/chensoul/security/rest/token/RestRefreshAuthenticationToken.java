package com.chensoul.security.rest.token;


import com.chensoul.security.util.SecurityUser;

public class RestRefreshAuthenticationToken extends AbstractRestAuthenticationToken {

	private static final long serialVersionUID = -1311042791508924523L;

	public RestRefreshAuthenticationToken(String refreshToken) {
		super(refreshToken);
	}

	public RestRefreshAuthenticationToken(SecurityUser securityUser) {
		super(securityUser);
	}
}

package com.chensoul.security.rest.mfa;

import com.chensoul.security.rest.token.AbstractRestAuthenticationToken;
import com.chensoul.security.util.SecurityUser;

public class MfaAuthenticationToken extends AbstractRestAuthenticationToken {
	public MfaAuthenticationToken(SecurityUser securityUser) {
		super(securityUser);
	}
}

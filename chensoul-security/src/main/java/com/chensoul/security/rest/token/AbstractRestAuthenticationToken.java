package com.chensoul.security.rest.token;

import com.chensoul.security.util.SecurityUser;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public abstract class AbstractRestAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -6212297506742428406L;

	private String token;

	private SecurityUser securityUser;

	public AbstractRestAuthenticationToken(String token) {
		super(null);
		this.token = token;
		this.setAuthenticated(false);
	}

	public AbstractRestAuthenticationToken(SecurityUser securityUser) {
		super(securityUser.getAuthorities());
		this.eraseCredentials();
		this.securityUser = securityUser;
		super.setAuthenticated(true);
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
				"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return this.securityUser;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.token = null;
	}

}

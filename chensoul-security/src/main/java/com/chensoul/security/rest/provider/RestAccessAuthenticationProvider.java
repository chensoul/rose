package com.chensoul.security.rest.provider;

import com.chensoul.security.rest.token.RestAccessAuthenticationToken;
import com.chensoul.security.support.TokenFactory;
import com.chensoul.security.util.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAccessAuthenticationProvider implements AuthenticationProvider {

	private final TokenFactory tokenFactory;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String accessToken = (String) authentication.getCredentials();
		SecurityUser securityUser = authenticate(accessToken);
		return new RestAccessAuthenticationToken(securityUser);
	}

	public SecurityUser authenticate(String accessToken) throws AuthenticationException {
		if (StringUtils.isEmpty(accessToken)) {
			throw new BadCredentialsException("Token is invalid");
		}
		return tokenFactory.parseAccessToken(accessToken);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (RestAccessAuthenticationToken.class.isAssignableFrom(authentication));
	}

}

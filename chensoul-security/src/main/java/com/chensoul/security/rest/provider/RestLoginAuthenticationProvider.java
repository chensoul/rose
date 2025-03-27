package com.chensoul.security.rest.provider;

import com.chensoul.security.rest.mfa.MfaAuthenticationToken;
import com.chensoul.security.rest.mfa.MfaProperties;
import com.chensoul.security.util.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@Slf4j
@RequiredArgsConstructor
public class RestLoginAuthenticationProvider implements AuthenticationProvider {
	private final UserDetailsService userDetailsService;
	private final PasswordEncoder encoder;
	private final MfaProperties mfaProperties;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No authentication data provided");

		String username = (String) authentication.getPrincipal();
		String password = (String) authentication.getCredentials();

		SecurityUser securityUser = authenticateByUsernameAndPassword(authentication, username, password);

		if (mfaProperties.isEnabled()) {
			return new MfaAuthenticationToken(securityUser);
		}

		return new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());
	}

	private SecurityUser authenticateByUsernameAndPassword(Authentication authentication, String username, String password) {
		UserDetails user = userDetailsService.loadUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found: " + username);
		}

		if (!encoder.matches(password, user.getPassword())) {
			throw new BadCredentialsException("Username or password not valid");
		}

		return new SecurityUser(user.getUsername(), user.getPassword(), user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
}

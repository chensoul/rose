package com.chensoul.security.support;

import com.chensoul.security.util.SecurityUser;
import com.chensoul.security.util.TokenPair;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
public interface TokenFactory {

	TokenPair createPreVerificationTokenPair(SecurityUser securityUser);

	TokenPair createTokenPair(SecurityUser securityUser);

	SecurityUser parseAccessToken(String accessToken);

	SecurityUser parseRefreshToken(String refreshToken);

}

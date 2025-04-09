package com.chensoul.security.support;

import com.chensoul.core.jackson.JacksonUtils;
import com.chensoul.security.SecurityProperties;
import com.chensoul.security.util.Authority;
import com.chensoul.security.util.SecurityUser;
import com.chensoul.security.util.TokenPair;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.UUID;

import static com.chensoul.security.CacheConstants.USER_REFRESH_TOKEN_PREFIX;
import static com.chensoul.security.CacheConstants.USER_TOKEN_PREFIX;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@RequiredArgsConstructor
public class RestTokenFactory implements TokenFactory {

	private final RedisTemplate<String, Object> redisTemplate;

	private final SecurityProperties securityProperties;

	@Override
	public TokenPair createPreVerificationTokenPair(SecurityUser securityUser) {
		String accessToken = RandomStringUtils.randomAlphabetic(20);
		redisTemplate.opsForValue()
			.set(USER_TOKEN_PREFIX + accessToken, securityUser, securityProperties.getAccessTokenExpireTime());
		return new TokenPair(accessToken, null,
				AuthorityUtils.createAuthorityList(Authority.PRE_VERIFICATION_TOKEN.name()));
	}

	@Override
	public TokenPair createTokenPair(SecurityUser securityUser) {
		String accessToken = UUID.randomUUID().toString();
		String refreshToken = UUID.randomUUID().toString();

		redisTemplate.opsForValue()
			.set(USER_TOKEN_PREFIX + accessToken, securityUser, securityProperties.getAccessTokenExpireTime());
		redisTemplate.opsForValue()
			.set(USER_REFRESH_TOKEN_PREFIX + refreshToken, accessToken, securityProperties.getRefreshTokenExpireTime());

		return new TokenPair(accessToken, refreshToken, securityUser.getAuthorities());
	}

	@Override
	public SecurityUser parseAccessToken(String accessToken) {
		Object securityUserObject = redisTemplate.opsForValue().get(USER_TOKEN_PREFIX + accessToken);
		if (securityUserObject == null) {
			throw new BadCredentialsException("Access token is invalid or has expired");
		}
		return JacksonUtils.fromString((String) securityUserObject, SecurityUser.class);
	}

	@Override
	public SecurityUser parseRefreshToken(String refreshToken) {
		Object accessToken = redisTemplate.opsForValue().get(USER_REFRESH_TOKEN_PREFIX + refreshToken);
		if (accessToken == null) {
			throw new BadCredentialsException("Refresh token is invalid or has expired");
		}
		return parseAccessToken((String) accessToken);
	}

}

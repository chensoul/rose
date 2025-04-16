package com.chensoul.rose.security.rest.provider;

import com.chensoul.rose.security.rest.token.RestRefreshAuthenticationToken;
import com.chensoul.rose.security.support.TokenFactory;
import com.chensoul.rose.security.util.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public class RestRefreshAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final TokenFactory tokenFactory;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.notNull(authentication, "No authentication data provided");
        String jwtToken = (String) authentication.getCredentials();
        SecurityUser unsafeUser = tokenFactory.parseRefreshToken(jwtToken);
        SecurityUser securityUser = authenticateByUserId(unsafeUser.getUsername());
        return new RestRefreshAuthenticationToken(securityUser);
    }

    private SecurityUser authenticateByUserId(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found by refresh token");
        }

        return new SecurityUser(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (RestRefreshAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

package com.chensoul.rose.security.rest.token;

import com.chensoul.rose.security.util.SecurityUser;

public class RestAccessAuthenticationToken extends AbstractRestAuthenticationToken {

    private static final long serialVersionUID = -8487219769037942225L;

    public RestAccessAuthenticationToken(String accessToken) {
        super(accessToken);
    }

    public RestAccessAuthenticationToken(SecurityUser securityUser) {
        super(securityUser);
    }
}

package com.chensoul.rose.security.rest.mfa;

import com.chensoul.rose.security.rest.token.AbstractRestAuthenticationToken;
import com.chensoul.rose.security.util.SecurityUser;

public class MfaAuthenticationToken extends AbstractRestAuthenticationToken {

    public MfaAuthenticationToken(SecurityUser securityUser) {
        super(securityUser);
    }
}

package com.chensoul.rose.security.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class AuthMethodNotSupportedException extends AuthenticationServiceException {

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }

    public AuthMethodNotSupportedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

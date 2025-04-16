package com.chensoul.rose.security.rest.param;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private String refreshToken;
}

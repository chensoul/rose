package com.chensoul.security.rest.param;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private String refreshToken;
}

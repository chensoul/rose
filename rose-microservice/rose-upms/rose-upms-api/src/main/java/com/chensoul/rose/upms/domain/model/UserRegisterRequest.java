package com.chensoul.rose.upms.domain.model;

import lombok.Data;

@Data
public class UserRegisterRequest {

    private String name;

    private String phone;

    private String password;

    private String secondPassword;
}

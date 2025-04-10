package com.chensoul.upms.model.param;

import lombok.Data;

@Data
public class UserRegisterRequest {

    private String name;

    private String phone;

    private String password;

    private String secondPassword;
}

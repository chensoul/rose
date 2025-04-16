package com.chensoul.rose.upms.domain.service;

public interface AuthService {

    void sendActivationSms(String phone);

    String getActivationLink(Long userId);
}

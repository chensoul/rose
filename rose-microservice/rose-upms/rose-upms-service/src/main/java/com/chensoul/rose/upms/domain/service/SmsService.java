package com.chensoul.rose.upms.domain.service;

public interface SmsService {

    void sendActivationSms(String url, String phone);
}

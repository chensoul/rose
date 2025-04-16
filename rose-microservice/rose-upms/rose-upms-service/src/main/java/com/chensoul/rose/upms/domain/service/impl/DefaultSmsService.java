package com.chensoul.rose.upms.domain.service.impl;

import com.chensoul.rose.upms.domain.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultSmsService implements SmsService {

    @Override
    public void sendActivationSms(String url, String phone) {
        log.info("{}, {}", url, phone);
    }
}

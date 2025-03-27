package com.chensoul.upms.service.impl;

import com.chensoul.upms.service.SmsService;
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

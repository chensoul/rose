package com.chensoul.upms.service;

public interface AuthService {

	void sendActivationSms(String phone);

	String getActivationLink(Long userId);

}

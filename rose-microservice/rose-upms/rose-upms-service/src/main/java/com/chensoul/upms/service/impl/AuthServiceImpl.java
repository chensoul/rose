package com.chensoul.upms.service.impl;

import static com.chensoul.upms.service.impl.UserServiceImpl.ACTIVATE_URL_PATTERN;

import com.chensoul.core.exception.BusinessException;
import com.chensoul.core.validation.Validators;
import com.chensoul.upms.entity.Credential;
import com.chensoul.upms.entity.User;
import com.chensoul.upms.service.AuthService;
import com.chensoul.upms.service.SmsService;
import com.chensoul.upms.service.SystemSettingService;
import com.chensoul.upms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final SystemSettingService systemSettingService;

    private final SmsService smsService;

    @Override
    public void sendActivationSms(String phone) {
        User user = userService.findUserByPhone(phone);
        Credential userCredential = Validators.checkNotNull(userService.findUserCredentialByUserId(user.getId()));
        if (!userCredential.isEnabled() && userCredential.getActivateToken() != null) {
            String baseUrl = systemSettingService.getBaseUrl();
            String activateUrl = String.format(ACTIVATE_URL_PATTERN, baseUrl, userCredential.getActivateToken());
            smsService.sendActivationSms(activateUrl, user.getPhone());
        } else {
            throw new BusinessException("用户已经激活！");
        }
    }

    @Override
    public String getActivationLink(Long userId) {
        Credential userCredential = Validators.checkNotNull(userService.findUserCredentialByUserId(userId));
        if (!userCredential.isEnabled() && userCredential.getActivateToken() != null) {
            String baseUrl = systemSettingService.getBaseUrl();
            return String.format(ACTIVATE_URL_PATTERN, baseUrl, userCredential.getActivateToken());
        } else {
            throw new BusinessException("用户已经激活！");
        }
    }
}

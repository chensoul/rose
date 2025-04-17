/*
 * Copyright © 2025 Chensoul, Inc. (ichensoul@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.upms.domain.service.impl;

import static com.chensoul.rose.upms.domain.service.impl.UserServiceImpl.ACTIVATE_URL_PATTERN;

import com.chensoul.rose.core.exception.BusinessException;
import com.chensoul.rose.core.validation.Validators;
import com.chensoul.rose.upms.domain.entity.Credential;
import com.chensoul.rose.upms.domain.entity.User;
import com.chensoul.rose.upms.domain.service.AuthService;
import com.chensoul.rose.upms.domain.service.SmsService;
import com.chensoul.rose.upms.domain.service.SystemSettingService;
import com.chensoul.rose.upms.domain.service.UserService;
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

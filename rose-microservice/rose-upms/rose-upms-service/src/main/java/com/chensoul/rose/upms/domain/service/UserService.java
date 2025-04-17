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
package com.chensoul.rose.upms.domain.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chensoul.rose.security.util.TokenPair;
import com.chensoul.rose.upms.domain.entity.Credential;
import com.chensoul.rose.upms.domain.entity.User;
import com.chensoul.rose.upms.domain.model.UserRegisterRequest;

public interface UserService extends IService<User> {

    User registerUser(UserRegisterRequest userRegisterRequest);

    User registerUser(UserRegisterRequest userRegisterRequest, boolean sendActivationSms);

    void deleteUser(User user);

    Credential findUserCredentialByUserId(Long userId);

    Credential saveUserCredential(Credential credential);

    User setUserCredentialEnabled(Long userId, boolean userCredentialsEnabled);

    TokenPair getUserToken(Long userId);

    Page<User> findUserByTenantId(Page page, String tenantId);

    User findUserByPhone(String phone);
}

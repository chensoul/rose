/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.upms.domain.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.rose.core.exception.BusinessException;
import com.chensoul.rose.core.jackson.JacksonUtils;
import com.chensoul.rose.core.spring.SpringContextHolder;
import com.chensoul.rose.security.support.TokenFactory;
import com.chensoul.rose.security.util.Authority;
import com.chensoul.rose.security.util.SecurityUser;
import com.chensoul.rose.security.util.TokenPair;
import com.chensoul.rose.upms.domain.entity.Credential;
import com.chensoul.rose.upms.domain.entity.User;
import com.chensoul.rose.upms.domain.mapper.CredentialMapper;
import com.chensoul.rose.upms.domain.mapper.UserMapper;
import com.chensoul.rose.upms.domain.mapper.UserSettingMapper;
import com.chensoul.rose.upms.domain.model.UserRegisterRequest;
import com.chensoul.rose.upms.domain.model.enums.UserStatus;
import com.chensoul.rose.upms.domain.model.event.DeleteEntityEvent;
import com.chensoul.rose.upms.domain.model.event.SaveEntityEvent;
import com.chensoul.rose.upms.domain.model.event.UserCacheEvictEvent;
import com.chensoul.rose.upms.domain.model.event.UserCredentialInvalidationEvent;
import com.chensoul.rose.upms.domain.service.SmsService;
import com.chensoul.rose.upms.domain.service.SystemSettingService;
import com.chensoul.rose.upms.domain.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public static final String USER_CREDENTIAL_ENABLED = "userCredentialEnabled";

    public static final String ACTIVATE_URL_PATTERN = "%s/api/noauth/activate?activateToken=%s";

    public static final String LAST_LOGIN_TIME = "lastLoginTime";

    public static final String FAILED_LOGIN_ATTEMPTS = "failedLoginAttempts";

    public static final String USER_PASSWORD_HISTORY = "userPasswordHistory";

    private static final int DEFAULT_TOKEN_LENGTH = 30;

    private final CredentialMapper credentialMapper;

    private final UserSettingMapper userSettingMapper;

    private final SystemSettingService systemSettingService;

    private final SmsService smsService;

    private final TokenFactory tokenFactory;

    // private final UserCachedEntityService userCachedEntityService;

    @Value("${security.user_token_access_enabled:true}")
    private boolean userTokenAccessEnabled;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(UserRegisterRequest userRegisterRequest) {
        log.trace("Executing registerUser [{}]", userRegisterRequest);

        User user = new User();
        user.setName(userRegisterRequest.getName());
        user.setPhone(userRegisterRequest.getPhone());
        user.setStatus(UserStatus.PENDING.getCode());
        user.setAuthority(Authority.NORMAL_USER);
        return saveUser(user, userRegisterRequest.getPassword());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User registerUser(UserRegisterRequest userRegisterRequest, boolean sendActivationSms) {
        User savedUser = registerUser(userRegisterRequest);

        if (sendActivationSms) {
            Credential credential = findUserCredentialByUserId(savedUser.getId());
            String baseUrl = systemSettingService.getBaseUrl();
            String activateUrl = String.format(ACTIVATE_URL_PATTERN, baseUrl, credential.getActivateToken());
            String phone = savedUser.getPhone();
            try {
                smsService.sendActivationSms(activateUrl, phone);
            } catch (BusinessException e) {
                deleteUser(savedUser);
                throw e;
            }
        }

        return savedUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(User user) {
        credentialMapper.deleteById(user.getId());
        userSettingMapper.deleteById(user.getId());
        baseMapper.deleteById(user.getId());

        SpringContextHolder.publishEvent(new UserCredentialInvalidationEvent(user.getId()));
        SpringContextHolder.publishEvent(
                DeleteEntityEvent.builder().entity(user).build());
    }

    @Override
    public Credential findUserCredentialByUserId(Long userId) {
        return credentialMapper.selectById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Credential saveUserCredential(Credential credential) {
        credentialMapper.insert(credential);
        return credential;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User setUserCredentialEnabled(Long userId, boolean userCredentialEnabled) {
        Credential credential = findUserCredentialByUserId(userId);
        credential.setEnabled(userCredentialEnabled);
        saveUserCredential(credential);

        User user = getById(userId);
        JsonNode extra = user.getExtra();
        if (!(extra instanceof ObjectNode)) {
            extra = JacksonUtils.newObjectNode();
        }
        ((ObjectNode) extra).put(USER_CREDENTIAL_ENABLED, userCredentialEnabled);
        user.setExtra(extra);

        if (userCredentialEnabled) {
            resetFailedLoginAttempts(user);
        } else {
            SpringContextHolder.publishEvent(new UserCredentialInvalidationEvent(userId));
        }
        return saveUser(user, null);
    }

    private User saveUser(User user, String password) {
        User oldUser = null;
        if (user.getId() != null) {
            oldUser = getById(user.getId());
        }

        UserCacheEvictEvent evictEvent =
                new UserCacheEvictEvent(user.getPhone(), oldUser != null ? oldUser.getPhone() : null);

        if (user.getId() == null) {
            user.setExtra(JacksonUtils.newObjectNode().put(USER_CREDENTIAL_ENABLED, false));
            baseMapper.insert(user);

            // userCachedEntityService.publishEvictEvent(evictEvent);

            Credential credential = new Credential();
            credential.setUserId(user.getId());
            credential.setEnabled(false);
            credential.setPassword(password);
            credential.setActivateToken(generateSafeToken(DEFAULT_TOKEN_LENGTH));
            credential.setUserId(user.getId());
            credential.setExtra(JacksonUtils.newObjectNode());
            credentialMapper.insert(credential);
        } else {
            baseMapper.updateById(user);
        }
        SpringContextHolder.publishEvent(SaveEntityEvent.builder()
                .entity(user)
                .oldEntity(oldUser)
                .created(user.getId() == null)
                .build());
        return user;
    }

    @Override
    public TokenPair getUserToken(Long userId) {
        if (!userTokenAccessEnabled) {
            throw new BusinessException("没有权限");
        }
        User user = getById(userId);
        SecurityUser securityUser = new SecurityUser(
                user.getName(),
                null,
                AuthorityUtils.createAuthorityList(user.getAuthority().name()));
        return tokenFactory.createTokenPair(securityUser);
    }

    @Override
    public Page<User> findUserByTenantId(Page page, String tenantId) {
        return null;
    }

    @Override
    public User findUserByPhone(String phone) {
        return baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
        // return userCachedEntityService.getCache().getAndPutInTransaction(phone,
        // () -> baseMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getPhone,
        // phone)), true);
    }

    private void resetFailedLoginAttempts(User user) {
        JsonNode extra = user.getExtra();
        if (!(extra instanceof ObjectNode)) {
            extra = JacksonUtils.newObjectNode();
        }
        ((ObjectNode) extra).put(FAILED_LOGIN_ATTEMPTS, 0);
        user.setExtra(extra);
    }

    private String generateSafeToken(int defaultTokenLength) {
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(
                        RandomStringUtils.randomAlphabetic(defaultTokenLength).getBytes(StandardCharsets.UTF_8));
    }
}

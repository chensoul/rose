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

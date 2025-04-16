package com.chensoul.rose.security.rest.mfa.provider;

import com.chensoul.rose.security.rest.mfa.config.MfaConfig;
import com.chensoul.rose.security.util.SecurityUser;
import org.springframework.security.core.userdetails.User;

public interface MfaProvider<C extends MfaProviderConfig, A extends MfaConfig> {

    A generateTwoFaConfig(User user, C providerConfig);

    default void prepareVerificationCode(SecurityUser user, C providerConfig, A accountConfig) {}

    boolean checkVerificationCode(SecurityUser user, String code, C providerConfig, A accountConfig);

    default void check(String tenantId) {}

    MfaProviderType getType();
}

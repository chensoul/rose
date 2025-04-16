package com.chensoul.rose.security.rest.mfa;

import com.chensoul.rose.security.util.TokenPair;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface MfaSettingService {

    void prepareVerificationCode() throws JsonProcessingException;

    TokenPair checkVerificationCode(String verificationCode);

    List<MfaAuthController.TwoFaProviderInfo> getAvailableTwoFaProviders();
}

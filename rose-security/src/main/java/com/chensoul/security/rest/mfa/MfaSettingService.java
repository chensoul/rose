package com.chensoul.security.rest.mfa;

import com.chensoul.security.util.TokenPair;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface MfaSettingService {

    void prepareVerificationCode() throws JsonProcessingException;

    TokenPair checkVerificationCode(String verificationCode);

    List<MfaAuthController.TwoFaProviderInfo> getAvailableTwoFaProviders();
}

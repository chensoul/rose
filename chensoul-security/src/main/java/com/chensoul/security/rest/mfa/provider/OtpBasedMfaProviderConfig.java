package com.chensoul.security.rest.mfa.provider;

import javax.validation.constraints.Min;
import lombok.Data;

@Data
public abstract class OtpBasedMfaProviderConfig implements MfaProviderConfig {

    @Min(value = 1, message = "is required")
    private int verificationCodeExpireTime;
}

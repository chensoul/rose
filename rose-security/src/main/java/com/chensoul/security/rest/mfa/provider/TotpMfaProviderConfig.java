package com.chensoul.security.rest.mfa.provider;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TotpMfaProviderConfig implements MfaProviderConfig {

    @NotBlank
    private String issuerName;

    @Override
    public MfaProviderType getProviderType() {
        return MfaProviderType.TOTP;
    }
}

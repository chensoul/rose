package com.chensoul.rose.security.rest.mfa.config;

import com.chensoul.rose.security.rest.mfa.provider.MfaProviderType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TotpMfaConfig extends MfaConfig {

    @NotBlank
    @Pattern(regexp = "otpauth://totp/(\\S+?):(\\S+?)\\?issuer=(\\S+?)&secret=(\\w+?)", message = "is invalid")
    private String authUrl;

    @Override
    public MfaProviderType getProviderType() {
        return MfaProviderType.TOTP;
    }
}

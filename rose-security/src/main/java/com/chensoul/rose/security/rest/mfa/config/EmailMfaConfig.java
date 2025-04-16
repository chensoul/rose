package com.chensoul.rose.security.rest.mfa.config;

import com.chensoul.rose.security.rest.mfa.provider.MfaProviderType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailMfaConfig extends OtpBasedMfaConfig {

    @NotBlank
    @Email
    private String email;

    @Override
    public MfaProviderType getProviderType() {
        return MfaProviderType.EMAIL;
    }
}

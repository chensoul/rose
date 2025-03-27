package com.chensoul.security.rest.mfa.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OtpBasedMfaConfig extends MfaConfig {
}

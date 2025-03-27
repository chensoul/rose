package com.chensoul.security.rest.mfa.provider;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public abstract class OtpBasedMfaProviderConfig implements MfaProviderConfig {

	@Min(value = 1, message = "is required")
	private int verificationCodeExpireTime;

}

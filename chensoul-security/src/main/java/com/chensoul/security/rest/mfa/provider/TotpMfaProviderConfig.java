package com.chensoul.security.rest.mfa.provider;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TotpMfaProviderConfig implements MfaProviderConfig {

	@NotBlank
	private String issuerName;

	@Override
	public MfaProviderType getProviderType() {
		return MfaProviderType.TOTP;
	}

}

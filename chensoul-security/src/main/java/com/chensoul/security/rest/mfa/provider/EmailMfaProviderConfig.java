package com.chensoul.security.rest.mfa.provider;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmailMfaProviderConfig extends OtpBasedMfaProviderConfig {

	@Override
	public MfaProviderType getProviderType() {
		return MfaProviderType.EMAIL;
	}

}

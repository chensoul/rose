package com.chensoul.security.rest.mfa.config;

import com.chensoul.security.rest.mfa.provider.MfaProviderType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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

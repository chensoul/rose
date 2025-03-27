package com.chensoul.security.rest.mfa.provider;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmsMfaProviderConfig extends OtpBasedMfaProviderConfig {

	@NotBlank(message = "is required")
	@Pattern(regexp = ".*\\$\\{code}.*", message = "must contain verification code")
	private String template;

	@Override
	public MfaProviderType getProviderType() {
		return MfaProviderType.SMS;
	}

}

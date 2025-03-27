package com.chensoul.security.rest.mfa.provider;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class BackupCodeMfaProviderConfig implements MfaProviderConfig {

	@Min(value = 1, message = "must be greater than 0")
	private int codesQuantity;

	@Override
	public MfaProviderType getProviderType() {
		return MfaProviderType.BACKUP_CODE;
	}

}

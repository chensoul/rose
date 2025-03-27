package com.chensoul.security.rest.mfa.config;

import com.chensoul.security.rest.mfa.provider.MfaProviderType;
import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class UserMfaSetting {
	private LinkedHashMap<MfaProviderType, MfaConfig> configs;
}

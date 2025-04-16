package com.chensoul.rose.security.rest.mfa.config;

import com.chensoul.rose.security.rest.mfa.provider.MfaProviderType;
import java.util.LinkedHashMap;
import lombok.Data;

@Data
public class UserMfaSetting {

    private LinkedHashMap<MfaProviderType, MfaConfig> configs;
}

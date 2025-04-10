package com.chensoul.security.rest.mfa;

import com.chensoul.core.jackson.JacksonUtils;
import com.chensoul.security.rest.mfa.config.MfaConfig;
import com.chensoul.security.rest.mfa.provider.MfaProviderConfig;
import com.chensoul.security.rest.mfa.provider.MfaProviderType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@ConfigurationProperties(prefix = "security.mfa", ignoreUnknownFields = false)
public class MfaProperties {

	private boolean enabled = false;

	@Valid
	@NotEmpty
	private List<Map<String, Object>> providers;

	@NotNull
	@Min(value = 5)
	private Integer minVerificationCodeSendPeriod;

	@Min(value = 0, message = "must be positive")
	private Integer maxVerificationFailuresBeforeUserLockout;

	@NotNull
	@Min(value = 60)
	private Long totalAllowedTimeForVerification = 3600L; // sec

	@Valid
	@NotNull
	private List<Map<String, Object>> configs;

	public List<MfaConfig> getAllConfigs() {
		return configs.stream()
			.map(twoFaConfig -> JacksonUtils.fromString(JacksonUtils.toString(twoFaConfig), MfaConfig.class))
			.collect(Collectors.toList());
	}

	public MfaConfig getDefaultConfig() {
		return getAllConfigs().stream().filter(MfaConfig::isUseByDefault).findAny().orElse(null);
	}

	public Optional<MfaProviderConfig> getProviderConfig(MfaProviderType providerType) {
		return Optional.ofNullable(providers)
			.flatMap(providersConfigs -> providersConfigs.stream()
				.map(providerConfig -> JacksonUtils.fromString(JacksonUtils.toString(providerConfig),
					MfaProviderConfig.class))
				.filter(providerConfig -> providerConfig.getProviderType().equals(providerType))
				.findFirst());
	}

}

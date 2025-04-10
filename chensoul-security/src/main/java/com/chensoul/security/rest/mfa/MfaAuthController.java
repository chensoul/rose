package com.chensoul.security.rest.mfa;

import com.chensoul.security.rest.mfa.provider.MfaProviderType;
import com.chensoul.security.util.TokenPair;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/mfa")
@RequiredArgsConstructor
public class MfaAuthController {

    private final MfaSettingService mfaSettingService;

    @PostMapping("/verification/send")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public void requestTwoFaVerificationCode() throws Exception {
        mfaSettingService.prepareVerificationCode();
    }

    @PostMapping("/verification/check")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public TokenPair checkTwoFaVerificationCode(@RequestParam String verificationCode) throws Exception {
        return mfaSettingService.checkVerificationCode(verificationCode);
    }

    @GetMapping("/providers")
    @PreAuthorize("hasAuthority('PRE_VERIFICATION_TOKEN')")
    public List<TwoFaProviderInfo> getAvailableTwoFaProviders() {
        return mfaSettingService.getAvailableTwoFaProviders();
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class TwoFaProviderInfo {

        private MfaProviderType type;

        private boolean useByDefault;

        private String contact;

        private Integer minVerificationCodeSendPeriod;
    }
}

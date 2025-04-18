/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.security.rest.mfa;

import com.chensoul.rose.security.rest.mfa.provider.MfaProviderType;
import com.chensoul.rose.security.util.TokenPair;
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

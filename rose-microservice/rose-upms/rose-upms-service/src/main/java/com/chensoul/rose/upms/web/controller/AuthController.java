package com.chensoul.rose.upms.web.controller;

import static com.chensoul.rose.upms.Constants.USER_ID;

import com.chensoul.rose.core.util.RestResponse;
import com.chensoul.rose.upms.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @PostMapping(value = "/auth/sendActivationSms")
    public RestResponse<Void> sendActivationSms(@RequestParam(value = "phone") String phone) {
        authService.sendActivationSms(phone);
        return RestResponse.ok();
    }

    // @PreAuthorize("hasAnyAuthority('SYS_ADMIN', 'TENANT_ADMIN')")
    @GetMapping(value = "/auth/{userId}/activationLink", produces = "text/plain")
    public String getActivationLink(@PathVariable(USER_ID) Long userId) {
        return authService.getActivationLink(userId);
    }
}

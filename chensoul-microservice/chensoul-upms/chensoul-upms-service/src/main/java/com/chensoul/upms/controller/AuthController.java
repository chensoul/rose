package com.chensoul.upms.controller;

import com.chensoul.core.util.RestResponse;
import com.chensoul.upms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.chensoul.upms.ControllerConstants.USER_ID;

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

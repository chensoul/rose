package com.chensoul.upms.controller;

import com.chensoul.core.util.RestResponse;
import com.chensoul.upms.entity.UserTenant;
import com.chensoul.upms.model.param.AddUserToTenantRequest;
import com.chensoul.upms.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

	private final TenantService tenantService;

	@PostMapping("/{tenantId}/user")
	public RestResponse<UserTenant> addUserToTenant(@PathVariable String tenantId,
													@Valid @RequestBody AddUserToTenantRequest request) {
		return RestResponse.ok(tenantService.addUserToTenant(tenantId, request));
	}

}

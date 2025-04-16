package com.chensoul.rose.upms.web.controller;

import com.chensoul.rose.core.util.RestResponse;
import com.chensoul.rose.upms.domain.entity.UserTenant;
import com.chensoul.rose.upms.domain.model.AddUserToTenantRequest;
import com.chensoul.rose.upms.domain.service.TenantService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    private final TenantService tenantService;

    @PostMapping("/{tenantId}/user")
    public RestResponse<UserTenant> addUserToTenant(
            @PathVariable String tenantId, @Valid @RequestBody AddUserToTenantRequest request) {
        return RestResponse.ok(tenantService.addUserToTenant(tenantId, request));
    }
}

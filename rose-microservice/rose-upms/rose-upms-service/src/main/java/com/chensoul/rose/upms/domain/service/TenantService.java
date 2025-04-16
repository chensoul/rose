package com.chensoul.rose.upms.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chensoul.rose.upms.domain.entity.Tenant;
import com.chensoul.rose.upms.domain.entity.UserTenant;
import com.chensoul.rose.upms.domain.model.AddUserToTenantRequest;

public interface TenantService extends IService<Tenant> {

    UserTenant addUserToTenant(String tenantId, AddUserToTenantRequest request);
}

package com.chensoul.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chensoul.upms.entity.Tenant;
import com.chensoul.upms.entity.UserTenant;
import com.chensoul.upms.model.param.AddUserToTenantRequest;

public interface TenantService extends IService<Tenant> {

	UserTenant addUserToTenant(String tenantId, AddUserToTenantRequest request);
}

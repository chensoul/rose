package com.chensoul.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.core.exception.BusinessException;
import com.chensoul.core.spring.SpringContextHolder;
import com.chensoul.upms.entity.Tenant;
import com.chensoul.upms.entity.User;
import com.chensoul.upms.entity.UserTenant;
import com.chensoul.upms.enums.UserStatus;
import com.chensoul.upms.event.SaveEntityEvent;
import com.chensoul.upms.mapper.TenantMapper;
import com.chensoul.upms.mapper.UserTenantMapper;
import com.chensoul.upms.model.param.AddUserToTenantRequest;
import com.chensoul.upms.service.TenantService;
import com.chensoul.upms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {
	private final UserService userService;
	private final UserTenantMapper userTenantMapper;

	@Override
	public UserTenant addUserToTenant(String tenantId, AddUserToTenantRequest request) {
		User user = userService.getById(request.getUserId());
		if (user == null) {
			throw new BusinessException("用户不存在");
		}

		Tenant tenant = getById(tenantId);
		if (tenant == null) {
			throw new BusinessException("租户不存在");
		}

		UserTenant userTenant = new UserTenant();
		userTenant.setUserId(request.getUserId());
		userTenant.setTenantId(tenantId);
		userTenant.setStatus(UserStatus.PENDING.getCode());
		userTenantMapper.insert(userTenant);

		SpringContextHolder.publishEvent(SaveEntityEvent.builder()
			.entity(userTenant)
			.oldEntity(null)
			.created(true).build());

		return userTenant;
	}
}

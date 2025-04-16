package com.chensoul.rose.upms.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.rose.core.exception.BusinessException;
import com.chensoul.rose.core.spring.SpringContextHolder;
import com.chensoul.rose.upms.domain.entity.Tenant;
import com.chensoul.rose.upms.domain.entity.User;
import com.chensoul.rose.upms.domain.entity.UserTenant;
import com.chensoul.rose.upms.domain.mapper.TenantMapper;
import com.chensoul.rose.upms.domain.mapper.UserTenantMapper;
import com.chensoul.rose.upms.domain.model.AddUserToTenantRequest;
import com.chensoul.rose.upms.domain.model.enums.UserStatus;
import com.chensoul.rose.upms.domain.model.event.SaveEntityEvent;
import com.chensoul.rose.upms.domain.service.TenantService;
import com.chensoul.rose.upms.domain.service.UserService;
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
                .created(true)
                .build());

        return userTenant;
    }
}

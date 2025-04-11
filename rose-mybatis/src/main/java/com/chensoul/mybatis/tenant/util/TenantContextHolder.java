package com.chensoul.mybatis.tenant.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.chensoul.core.exception.BusinessException;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class TenantContextHolder {

    private final ThreadLocal<String> THREAD_LOCAL_TENANT = new TransmittableThreadLocal<>();

    private final ThreadLocal<Boolean> THREAD_LOCAL_IGNORED = TransmittableThreadLocal.withInitial(() -> false);

    public void setIgnore(Boolean ignored) {
        THREAD_LOCAL_IGNORED.set(ignored);
    }

    public String getTenantId() {
        return THREAD_LOCAL_TENANT.get();
    }

    public void setTenantId(String tenantId) {
        THREAD_LOCAL_TENANT.set(tenantId);
    }

    public String getRequiredTenantId() {
        String tenantId = getTenantId();
        if (StringUtils.isBlank(tenantId)) {
            throw new BusinessException("TenantContextHolder不存在租户");
        }
        return tenantId;
    }

    public Boolean isIgnored() {
        return THREAD_LOCAL_IGNORED.get();
    }

    public void clear() {
        THREAD_LOCAL_TENANT.remove();
        THREAD_LOCAL_IGNORED.remove();
    }
}

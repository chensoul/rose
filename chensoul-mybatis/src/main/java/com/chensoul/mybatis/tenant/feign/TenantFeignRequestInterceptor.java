package com.chensoul.mybatis.tenant.feign;

import com.chensoul.mybatis.tenant.util.TenantContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.chensoul.core.CommonConstants.HEADER_TENANT_ID;

@Slf4j
public class TenantFeignRequestInterceptor implements RequestInterceptor {

	@Override
	public void apply(RequestTemplate requestTemplate) {
		String tenantId = TenantContextHolder.getTenantId();
		if (StringUtils.isNotBlank(tenantId)) {
			requestTemplate.header(HEADER_TENANT_ID, tenantId);
		}
	}

}

package com.chensoul.mybatis.tenant.filter;

import com.chensoul.core.spring.WebUtils;
import com.chensoul.mybatis.tenant.util.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.chensoul.core.CommonConstants.HEADER_TENANT_ID;

@Slf4j
public class TenantContextFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String tenantId = WebUtils.getValue(request, HEADER_TENANT_ID);
		if (tenantId != null) {
			log.info("Visit {} with tenantId {}", request.getRequestURI(), tenantId);
			TenantContextHolder.setTenantId(tenantId);
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			TenantContextHolder.clear();
		}
	}
}

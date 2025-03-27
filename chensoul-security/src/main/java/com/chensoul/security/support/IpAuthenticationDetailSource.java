package com.chensoul.security.support;

import com.chensoul.core.spring.WebUtils;
import com.chensoul.core.util.NetUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public class IpAuthenticationDetailSource implements
	AuthenticationDetailsSource<HttpServletRequest, IpAuthenticationDetailSource.RestAuthenticationDetail> {

	@Override
	public RestAuthenticationDetail buildDetails(HttpServletRequest request) {
		return new RestAuthenticationDetail(WebUtils.getRemoteIp(request), NetUtils.getLocalhostStr());
	}

	@Data
	@RequiredArgsConstructor
	public static class RestAuthenticationDetail implements Serializable {
		private final String serverAddress;
		private final String clientAddress;
	}
}

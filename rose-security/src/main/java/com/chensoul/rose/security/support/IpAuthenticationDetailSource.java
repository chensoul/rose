package com.chensoul.rose.security.support;

import com.chensoul.rose.core.spring.WebUtils;
import com.chensoul.rose.core.util.NetUtils;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationDetailsSource;

public class IpAuthenticationDetailSource
        implements AuthenticationDetailsSource<
                HttpServletRequest, IpAuthenticationDetailSource.RestAuthenticationDetail> {

    @Override
    public RestAuthenticationDetail buildDetails(HttpServletRequest request) {
        return new RestAuthenticationDetail(WebUtils.getClientIp(request), NetUtils.getLocalhostStr());
    }

    @Data
    @RequiredArgsConstructor
    public static class RestAuthenticationDetail implements Serializable {

        private final String serverAddress;

        private final String clientAddress;
    }
}

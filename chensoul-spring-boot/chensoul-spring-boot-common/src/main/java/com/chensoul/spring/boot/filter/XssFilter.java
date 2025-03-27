package com.chensoul.spring.boot.filter;


import com.chensoul.core.util.EscapeUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 防止XSS攻击的过滤器
 */
@RequiredArgsConstructor
public class XssFilter implements Filter {
    public final List<String> excludes;

    public static boolean matches(String str, List<String> strs) {
        if (ObjectUtils.isEmpty(str) || ObjectUtils.isEmpty(strs)) {
            return false;
        }
        for (String pattern : strs) {
            if (new AntPathMatcher().match(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getServletPath();
        String method = request.getMethod();
        // GET DELETE 不过滤
        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            return true;
        }
        return matches(url, excludes);
    }

    public static class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
        public XssHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values != null) {
                int length = values.length;
                String[] escapeValues = new String[length];
                for (int i = 0; i < length; i++) {
                    // 防xss攻击和过滤前后空格
                    escapeValues[i] = EscapeUtils.clean(values[i]).trim();
                }
                return escapeValues;
            }
            return super.getParameterValues(name);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            // 非json类型，直接返回
            if (!isJsonRequest()) {
                return super.getInputStream();
            }

            // 为空，直接返回
            String json = IOUtils.toString(super.getInputStream(), "utf-8");
            if (StringUtils.isEmpty(json)) {
                return super.getInputStream();
            }

            // xss过滤
            json = EscapeUtils.clean(json).trim();
            byte[] jsonBytes = json.getBytes("utf-8");
            final ByteArrayInputStream bis = new ByteArrayInputStream(jsonBytes);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return true;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public int available() throws IOException {
                    return jsonBytes.length;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return bis.read();
                }
            };
        }

        public boolean isJsonRequest() {
            String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
            return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
        }
    }
}

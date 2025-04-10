package com.chensoul.spring.boot.filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author zhijun.chen
 * @since 2.16.3
 */
@Slf4j
public class CachingRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ServletRequest requestWrapper = new ContentCachingRequestWrapper(request);
        chain.doFilter(requestWrapper, response);
    }

    public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {
        private final byte[] body;

        public ContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            this.body = readRequestBody(request);
        }

        private byte[] readRequestBody(HttpServletRequest request) throws IOException {
            request.setCharacterEncoding("UTF-8");
            try (InputStream inputStream = request.getInputStream()) {
                return StreamUtils.copyToByteArray(inputStream);
            }
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    throw new UnsupportedOperationException("ReadListener is not supported");
                }
            };
        }
    }
}

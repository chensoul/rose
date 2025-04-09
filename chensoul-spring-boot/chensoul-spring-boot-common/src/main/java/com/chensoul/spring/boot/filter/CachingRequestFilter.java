package com.chensoul.spring.boot.filter;

import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zhijun.chen
 * @since 2.16.3
 */
public class CachingRequestFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletRequest requestWrapper = new ContentCachingRequestWrapper(request);
		chain.doFilter(requestWrapper, response);
	}

	public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {
		private final byte[] body;

		public ContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			request.setCharacterEncoding("UTF-8");
			body = StreamUtils.copyToByteArray(request.getInputStream());
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return new BufferedReader(new InputStreamReader(getInputStream()));
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			final ByteArrayInputStream bais = new ByteArrayInputStream(body);
			return new ServletInputStream() {
				@Override
				public int read() throws IOException {
					return bais.read();
				}

				@Override
				public boolean isFinished() {
					return Boolean.FALSE;
				}

				@Override
				public boolean isReady() {
					return Boolean.FALSE;
				}

				@Override
				public void setReadListener(ReadListener readListener) {
				}
			};
		}
	}
}

package com.chensoul.spring.boot.filter;

import com.chensoul.core.spring.WebUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request Body 缓存 Filter，实现它的可重复读取
 */
public class CachingRequestFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws IOException, ServletException {
		filterChain.doFilter(new ContentCachingRequestWrapper(request), response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		// 只处理 jackson 请求内容
		return !WebUtils.isJsonRequest(request);
	}
}

package com.chensoul.spring.boot.filter;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.chensoul.core.spring.WebUtils.isJsonRequest;

public class TraceFilter extends OncePerRequestFilter {

	public static final String TRACE_ID = "trace-id";

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !isJsonRequest(request);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader(TRACE_ID, TraceContext.traceId());

		filterChain.doFilter(request, response);
	}

}

package com.chensoul.spring.boot.filter;

import com.chensoul.core.exception.MaxPayloadSizeExceededException;
import com.chensoul.core.jackson.JacksonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class PayloadSizeFilter extends OncePerRequestFilter {

	private final Map<String, Long> limits = new LinkedHashMap<>();

	private final AntPathMatcher pathMatcher = new AntPathMatcher();

	public PayloadSizeFilter(String limitsConfiguration) {
		for (String limit : limitsConfiguration.split(";")) {
			try {
				String urlPathPattern = limit.split("=")[0];
				long maxPayloadSize = Long.parseLong(limit.split("=")[1]);
				limits.put(urlPathPattern, maxPayloadSize);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Failed to parse size limits configuration: " + limitsConfiguration);
			}
		}
		log.info("Initialized payload size filter with configuration: {}", limitsConfiguration);
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		for (String url : limits.keySet()) {
			if (pathMatcher.match(url, request.getRequestURI())) {
				if (checkMaxPayloadSizeExceeded(request, response, limits.get(url))) {
					return;
				}
				break;
			}
		}
		chain.doFilter(request, response);
	}

	private boolean checkMaxPayloadSizeExceeded(HttpServletRequest request, HttpServletResponse response,
			long maxPayloadSize) throws IOException {
		if (request.getContentLength() > maxPayloadSize) {
			log.info("[{}] [{}] Payload size {} exceeds the limit of {} bytes", request.getRemoteAddr(),
					request.getRequestURL(), request.getContentLength(), maxPayloadSize);
			handleMaxPayloadSizeExceededException(response, new MaxPayloadSizeExceededException(maxPayloadSize));
			return true;
		}
		return false;
	}

	@Override
	protected boolean shouldNotFilterAsyncDispatch() {
		return false;
	}

	@Override
	protected boolean shouldNotFilterErrorDispatch() {
		return false;
	}

	private void handleMaxPayloadSizeExceededException(HttpServletResponse response,
			MaxPayloadSizeExceededException exception) throws IOException {
		response.setStatus(HttpStatus.PAYLOAD_TOO_LARGE.value());
		JacksonUtils.writeValue(response.getWriter(), exception.getMessage());
	}

}

package com.chensoul.spring.boot.filter;

import com.chensoul.core.util.date.DatePattern;
import com.chensoul.core.util.date.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Setter
@RequiredArgsConstructor
public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {

	public static final String START_TIME = "x-request-start-time";

	private final int maxResponseTimeToLogInMs;

	private List<String> ignoreHeaders = Arrays.asList("password", "authorization", "token", "accessToken",
		"access_token", "refreshToken");

	@PostConstruct
	public void init() {
		Predicate<String> headerPredicate = headerName -> ObjectUtils.isEmpty(ignoreHeaders)
			|| !ignoreHeaders.contains(headerName);
		if (getHeaderPredicate() == null) {
			setHeaderPredicate(headerPredicate);
		} else {
			setHeaderPredicate(getHeaderPredicate().or(headerPredicate));
		}
	}

	@Override
	protected boolean shouldLog(HttpServletRequest request) {
		return true;
	}

	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		request.setAttribute(START_TIME, System.currentTimeMillis());
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		final Long startTime = (Long) request.getAttribute(START_TIME);
		if (startTime != null) {
			final long cost = System.currentTimeMillis() - startTime;
			message = message + ", cost " + cost + " ms";

			if (cost >= this.maxResponseTimeToLogInMs) {
				String execTime = TimeUtils.format(TimeUtils.fromMilliseconds(startTime),
					DatePattern.NORM_DATETIME_MS_PATTERN);
				log.warn("[SLOW_REQUEST] {} {} {} {}", execTime, request.getMethod(), request.getRequestURI(), cost);
			}
		}
		log.info(message);
	}

}

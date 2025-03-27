package com.chensoul.spring.boot.feign;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;

public class RetryMetricsErrorDecoder extends MetricsErrorDecoder {
	@Override
	public Exception decode(String methodKey, Response response) {
		metrics(methodKey);

		FeignException exception = feign.FeignException.errorStatus(methodKey, response);
		int status = response.status();
		if (status >= 400) {
			return new RetryableException(
				response.status(),
				exception.getMessage(),
				response.request().httpMethod(),
				exception,
				null,
				response.request());
		}
		return exception;
	}
}

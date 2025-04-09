package com.chensoul.core.exception;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public class TooManyRequestException extends RuntimeException {

	public TooManyRequestException(final String message) {
		super(message);
	}

	/**
	 * <p>
	 * Constructor for RequestNotPermittedException.
	 * </p>
	 * @param message a {@link String} object
	 * @param cause a {@link Throwable} object
	 */
	public TooManyRequestException(final String message, final Throwable cause) {
		super(message, cause);
	}

}

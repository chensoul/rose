package com.chensoul.rose.core.exception;

import lombok.EqualsAndHashCode;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@EqualsAndHashCode(callSuper = true)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}

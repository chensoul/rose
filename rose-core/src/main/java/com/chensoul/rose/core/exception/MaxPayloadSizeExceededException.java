package com.chensoul.rose.core.exception;

import lombok.Getter;

@Getter
public class MaxPayloadSizeExceededException extends RuntimeException {

    private final long limit;

    public MaxPayloadSizeExceededException(long limit) {
        super("Payload size exceeds the limit of " + limit + " bytes");
        this.limit = limit;
    }
}

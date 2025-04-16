package com.chensoul.rose.iot.model;

import lombok.Data;

@Data
public class ThingModeService {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String identifier;

    private String name;

    /**
     * async
     */
    private String callType;

    private boolean required;

    private String method;

    private String inputData;

    private String outputData;

    private String description;
}

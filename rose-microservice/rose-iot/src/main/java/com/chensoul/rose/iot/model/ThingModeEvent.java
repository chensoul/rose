package com.chensoul.rose.iot.model;

import lombok.Data;

/**
 *
 */
@Data
public class ThingModeEvent {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String identifier;

    private String name;

    private boolean required;

    /**
     * info,alarm,error
     */
    private String type;

    /**
     * thing.event.xxxx.post
     */
    private String method;

    private String description;

    private String outputData;
}

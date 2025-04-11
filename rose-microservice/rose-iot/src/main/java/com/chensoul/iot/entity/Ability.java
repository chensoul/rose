package com.chensoul.iot.entity;

import lombok.Data;

/**
 * TODO Comment
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since TODO
 */
@Data
public class Ability {

    private Long id;

    /**
     * 1: properties; 2: service;3: event
     */
    private Integer type;

    private Long categoryId;

    private String identifier;

    private String name;

    private boolean required;

    private boolean std;

    private String description;
}

package com.chensoul.rose.iot.model;

import lombok.Data;

@Data
public class TopicTemplate {

    private static final long serialVersionUID = 1L;

    /**
     * basic,model,custom
     */
    private String type;

    private String groupName;

    private String name;

    private String description;
}

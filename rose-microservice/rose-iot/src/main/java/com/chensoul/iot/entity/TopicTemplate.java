package com.chensoul.iot.entity;

import lombok.Data;

/**
 * Topic模板对象 topic_template
 */
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

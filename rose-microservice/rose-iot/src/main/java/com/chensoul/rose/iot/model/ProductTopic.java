package com.chensoul.rose.iot.model;

import lombok.Data;

@Data
public class ProductTopic {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String topicType;

    private String topicName;

    private String groupName;

    private String topicDescription;
}

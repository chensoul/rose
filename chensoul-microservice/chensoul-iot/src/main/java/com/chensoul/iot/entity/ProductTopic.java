package com.chensoul.iot.entity;

import lombok.Data;

/**
 * 产品Topic对象 product_topic
 */
@Data
public class ProductTopic {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String topicType;

    private String topicName;

    private String groupName;

    private String topicDescription;
}

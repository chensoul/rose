package com.chensoul.iot.entity;

import lombok.Data;

/**
 * 产品Topic对象 product_topic
 */
@Data
public class DeviceGroup {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String parentOd;
}

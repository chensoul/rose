package com.chensoul.rose.iot.model;

import lombok.Data;

@Data
public class DeviceGroup {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String parentOd;
}

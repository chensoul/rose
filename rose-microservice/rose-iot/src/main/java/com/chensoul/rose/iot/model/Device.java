package com.chensoul.rose.iot.model;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Device {

    private static final long serialVersionUID = 1L;

    private Long productId;

    private String code;

    private String name;

    private String nodeType;

    private Long groupId;

    /**
     * UNACTIVE
     */
    private String status;

    private LocalDateTime activeTime;

    private LocalDateTime lastOnlineTime;

    private LocalDateTime lastOfflineTime;
}

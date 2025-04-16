package com.chensoul.rose.upms.domain.model.enums;

import lombok.Getter;

@Getter
public enum TenantStatus {
    PENDING(1),
    ACTIVE(2),
    SUSPENDED(3),
    EXPIRED(4);

    private final int code;

    TenantStatus(int code) {
        this.code = code;
    }
}

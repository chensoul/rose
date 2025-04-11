package com.chensoul.upms.event;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeleteEntityEvent<T> {

    private final String tenantId;

    private final Serializable entityId;

    private final T entity;

    private final String body;

    @Builder.Default
    private final long ts = System.currentTimeMillis();
}

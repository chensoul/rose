package com.chensoul.upms.event;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SaveEntityEvent<T> {

    private final T entity;

    private final T oldEntity;

    private final Serializable entityId;

    private final Boolean created;
}

package com.chensoul.upms.event;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class SaveEntityEvent<T> {

	private final T entity;

	private final T oldEntity;

	private final Serializable entityId;

	private final Boolean created;

}

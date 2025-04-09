package com.chensoul.upms.event;

import lombok.Data;

import java.io.Serializable;

@Data
public final class UserCacheEvictEvent implements Serializable {

	private final String newPhone;

	private final String oldPhone;

}

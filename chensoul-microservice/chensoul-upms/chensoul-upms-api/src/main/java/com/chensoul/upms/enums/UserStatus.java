package com.chensoul.upms.enums;

import lombok.Getter;

@Getter
public enum UserStatus {

	PENDING(1), ACTIVE(2), SUSPENDED(3), LOCKED(4);

	private final int code;

	UserStatus(int code) {
		this.code = code;
	}

}

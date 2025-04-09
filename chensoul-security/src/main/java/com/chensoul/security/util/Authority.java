package com.chensoul.security.util;

import lombok.Getter;

@Getter
public enum Authority {

	SYS_ADMIN(0), TENANT_ADMIN(1), CUSTOMER_USER(2), NORMAL_USER(3), REFRESH_TOKEN(10), PRE_VERIFICATION_TOKEN(11);

	private final int code;

	Authority(int code) {
		this.code = code;
	}

	public static Authority parse(String value) {
		Authority authority = null;
		if (value != null && !value.isEmpty()) {
			for (Authority current : Authority.values()) {
				if (current.name().equalsIgnoreCase(value)) {
					authority = current;
					break;
				}
			}
		}
		return authority;
	}

}

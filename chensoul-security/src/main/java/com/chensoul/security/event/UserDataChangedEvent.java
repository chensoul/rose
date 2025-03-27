package com.chensoul.security.event;

import java.io.Serializable;

public abstract class UserDataChangedEvent implements Serializable {
	public abstract String getId();

	public abstract long getTs();
}

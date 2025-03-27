package com.chensoul.upms.event;

import java.io.Serializable;

public abstract class UserAuthDataChangedEvent implements Serializable {
	public abstract Long getId();

	public abstract long getTs();
}

package com.chensoul.rose.upms.domain.model.event;

import java.io.Serializable;

public abstract class UserAuthDataChangedEvent implements Serializable {

    public abstract Long getId();

    public abstract long getTs();
}

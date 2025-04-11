package com.chensoul.upms.event;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class UserCredentialInvalidationEvent extends UserAuthDataChangedEvent {

    private final Long userId;

    private final long ts;

    public UserCredentialInvalidationEvent(Long userId) {
        this.userId = userId;
        this.ts = System.currentTimeMillis();
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public long getTs() {
        return ts;
    }
}

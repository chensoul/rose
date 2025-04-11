package com.chensoul.core.domain;

public interface HasVersion {

    Long getVersion();

    default void setVersion(Long version) {}
}

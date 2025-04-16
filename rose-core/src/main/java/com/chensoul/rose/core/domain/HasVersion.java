package com.chensoul.rose.core.domain;

public interface HasVersion {

    Long getVersion();

    default void setVersion(Long version) {}
}

package com.chensoul.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
@Data
public class EntityInfo implements HasId<Serializable>, HasName {

    private final Serializable id;

    private final String name;

    @JsonCreator
    public EntityInfo(@JsonProperty("id") Serializable id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}

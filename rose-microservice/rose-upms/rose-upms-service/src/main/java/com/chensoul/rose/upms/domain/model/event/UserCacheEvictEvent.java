package com.chensoul.rose.upms.domain.model.event;

import java.io.Serializable;
import lombok.Data;

@Data
public final class UserCacheEvictEvent implements Serializable {

    private final String newPhone;

    private final String oldPhone;
}

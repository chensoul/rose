package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.rose.core.jackson.serializer.sensitive.FieldSensitive;
import com.chensoul.rose.core.jackson.serializer.sensitive.SensitiveType;
import com.chensoul.rose.mybatis.model.BaseEntity;
import com.chensoul.rose.security.util.Authority;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    @FieldSensitive(type = SensitiveType.PHONE)
    private String phone;

    private JsonNode extra;

    private String lastLoginIp;

    private LocalDateTime lastLoginTime;

    private Authority authority;

    // 1: Pending 待激活, 2: Active 激活, 3: Suspended 停用, 4: Locked 锁定
    private Integer status;
}

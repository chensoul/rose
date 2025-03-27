package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.core.jackson.serializer.sensitive.FieldSensitive;
import com.chensoul.core.jackson.serializer.sensitive.SensitiveType;
import com.chensoul.mybatis.model.BaseEntity;
import com.chensoul.security.util.Authority;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

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

package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.mybatis.model.BaseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_credential")
public class Credential extends BaseEntity {
	private static final long serialVersionUID = -2108436378880529163L;

	private Long userId;
	private boolean enabled;
	private String password;
	private String activateToken;
	private String resetToken;

	private JsonNode extra;
}

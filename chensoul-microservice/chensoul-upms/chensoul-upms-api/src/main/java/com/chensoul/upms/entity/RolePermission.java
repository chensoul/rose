package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

import static com.chensoul.core.util.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@Accessors(chain = true)
@TableName("sys_role_permission")
public class RolePermission implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long roleId;

	private Long permissionId;

	@TableField(fill = FieldFill.INSERT)
	private String tenantId;

	@TableField(value = "created_by", fill = FieldFill.INSERT)
	private String createdBy;

	@JsonFormat(pattern = NORM_DATETIME_PATTERN)
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}

package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

import static com.chensoul.core.util.date.DatePattern.NORM_DATETIME_PATTERN;

@Data
@TableName("sys_user_position")
public class UserPosition {

	private Long userId;

	private Long positionId;

	@TableField(fill = FieldFill.INSERT)
	private String tenantId;

	@TableField(value = "created_by", fill = FieldFill.INSERT)
	private String createdBy;

	@JsonFormat(pattern = NORM_DATETIME_PATTERN)
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private LocalDateTime createTime;

}

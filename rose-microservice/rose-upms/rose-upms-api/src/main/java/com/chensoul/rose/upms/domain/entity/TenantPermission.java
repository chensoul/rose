package com.chensoul.rose.upms.domain.entity;

import static com.chensoul.rose.core.util.date.DatePattern.NORM_DATETIME_PATTERN;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@TableName("sys_tenant_permission")
public class TenantPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long permissionId;

    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private String createdBy;

    @JsonFormat(pattern = NORM_DATETIME_PATTERN)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

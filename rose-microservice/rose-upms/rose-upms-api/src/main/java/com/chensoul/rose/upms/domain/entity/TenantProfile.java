package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.chensoul.rose.mybatis.model.TenantEntity;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户套餐
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "sys_tenant_profile", autoResultMap = true)
public class TenantProfile extends TenantEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String description;

    private boolean isDefault;

    private LocalDateTime expireTime;

    /**
     * 套餐限制数量： 最大应用数 最大资源数 最大用户数 最大角色数
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode content;
}

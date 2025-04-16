package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.chensoul.rose.mybatis.model.BaseEntity;
import com.chensoul.rose.upms.domain.model.enums.SystemSettingType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "sys_system_setting", autoResultMap = true)
public class SystemSetting extends BaseEntity {

    private static final long serialVersionUID = -7670322981725511892L;

    private SystemSettingType type;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode content;
}

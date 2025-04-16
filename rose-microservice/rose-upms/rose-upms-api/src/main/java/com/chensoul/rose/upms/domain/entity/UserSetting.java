package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.chensoul.rose.mybatis.model.BaseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user_setting", autoResultMap = true)
public class UserSetting extends BaseEntity {

    private static final long serialVersionUID = 2628320657987010348L;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotBlank(message = "key不能为空")
    private String key;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private JsonNode value;
}

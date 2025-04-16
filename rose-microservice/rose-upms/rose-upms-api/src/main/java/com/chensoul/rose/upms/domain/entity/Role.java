package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.rose.mybatis.model.TenantEntity;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_role")
public class Role extends TenantEntity {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "角色名字不能为空")
    private String name;

    private String code;

    private Integer sort;

    private String description;

    private Integer status;
}

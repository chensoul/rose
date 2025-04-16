package com.chensoul.rose.mybatis.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TenantEntity extends BaseEntity implements Serializable {

    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
}

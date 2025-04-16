package com.chensoul.rose.mybatis.model;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseEntity extends AuditEntity implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    protected Long id;

    @Version
    protected Integer version;

    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    protected boolean isDeleted = false;
}

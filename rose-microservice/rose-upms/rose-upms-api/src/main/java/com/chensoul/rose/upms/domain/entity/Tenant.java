package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.rose.mybatis.model.AuditEntity;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_tenant")
public class Tenant extends AuditEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    @NotBlank(message = "租户编号不能为空")
    @Length(max = 20, message = "租户编号长度不能超过20")
    private String id;

    @NotBlank(message = "租户名称不能为空")
    @Length(max = 20, message = "租户名称长度不能超过20")
    private String name;

    private String domain;

    @Length(max = 256, message = "地址长度不能超过256")
    private String address;

    // 行业
    private String industry;

    private LocalDateTime expireTime;

    private String contactName;

    private String contactPhone;

    private Integer status;
}

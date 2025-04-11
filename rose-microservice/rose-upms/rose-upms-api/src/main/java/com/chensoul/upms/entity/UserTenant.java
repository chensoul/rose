package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.core.jackson.serializer.sensitive.FieldSensitive;
import com.chensoul.core.jackson.serializer.sensitive.SensitiveType;
import com.chensoul.mybatis.model.TenantEntity;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_tenant")
public class UserTenant extends TenantEntity {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String avatar;

    @FieldSensitive(type = SensitiveType.EMAIL)
    private String email;

    private String departmentId;

    private LocalDateTime joinTime;

    // 1: Pending 待审核, 2: Active 激活, 3: Suspended 停用, 4: Locked 锁定
    private Integer status;
}

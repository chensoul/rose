package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.mybatis.model.TenantEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
public class Log extends TenantEntity implements Serializable {

    private static final long serialVersionUID = 1129753896999673095L;

    private String name;

    private String serverIp;

    private String remoteIp;

    private String remoteLocation;

    private String userAgent;

    private String requestUrl;

    private String requestParams;

    private String requestMethod;

    private String responseResult;

    private Long costTime;

    private boolean success;

    private String exception;

    private String traceId;
}

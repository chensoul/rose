package com.chensoul.rose.mybatis;

import java.util.Collections;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mybatis-plus.tenant")
public class TenantProperties {

    private boolean enabled = true;

    private String tenantIdColumn = "tenant_id";

    /**
     * 全路径：com.xxx.user.mybatis-mapper.SysUserMapper.findList
     */
    private Set<String> ignoredMappedStatements = Collections.emptySet();

    /**
     * 需要忽略多租户的请求
     * <p>
     * 默认情况下，每个请求需要带上 tenant-id 的请求头。但是，部分请求是无需带上的，例如说短信回调、支付回调等 Open API！
     */
    private Set<String> ignoreUrls = Collections.emptySet();

    /**
     * 需要忽略多租户的表
     * <p>
     * 即默认所有表都开启多租户的功能，所以记得添加对应的 tenant_id 字段哟
     */
    private Set<String> ignoredTables = Collections.emptySet();

    /**
     * 需要忽略多租户的 Spring Cache 缓存
     * <p>
     * 即默认所有缓存都开启多租户的功能，所以记得添加对应的 tenant_id 字段哟
     */
    private Set<String> ignoredCaches = Collections.emptySet();
}

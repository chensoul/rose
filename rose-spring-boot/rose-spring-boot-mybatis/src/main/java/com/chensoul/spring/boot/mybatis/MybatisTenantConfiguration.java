package com.chensoul.spring.boot.mybatis;

import static com.chensoul.core.CommonConstants.TENANT_CONTEXT_FILTER;
import static com.chensoul.core.CommonConstants.TENANT_SECURITY_FILTER;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.chensoul.core.spring.WebUtils;
import com.chensoul.mybatis.tenant.aspect.TenantIgnoreAspect;
import com.chensoul.mybatis.tenant.aspect.TenantJobAspect;
import com.chensoul.mybatis.tenant.feign.TenantFeignRequestInterceptor;
import com.chensoul.mybatis.tenant.filter.TenantContextFilter;
import com.chensoul.mybatis.tenant.filter.TenantSecurityFilter;
import com.chensoul.mybatis.tenant.handler.DefaultTenantLineHandler;
import com.chensoul.mybatis.tenant.handler.TenantMetaObjectHandler;
import com.chensoul.mybatis.tenant.service.TenantService;
import com.chensoul.mybatis.util.MyBatisUtils;
import com.chensoul.spring.boot.mybatis.redis.TenantRedisCacheManager;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Configuration
@AutoConfigureBefore({MybatisCoreConfiguration.class})
@EnableConfigurationProperties({TenantProperties.class})
@ConditionalOnProperty(name = "mybatis-plus.tenant.enabled", havingValue = "true", matchIfMissing = true)
public class MybatisTenantConfiguration {

    public MybatisTenantConfiguration() {
        log.info("Initializing MybatisTenantConfiguration");
    }

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    @Bean
    @ConditionalOnBean(TenantService.class)
    @ConditionalOnClass(name = "com.xxl.job.core.context.XxlJobHelper")
    public TenantJobAspect tenantJobAspect(TenantService tenantService) {
        return new TenantJobAspect(tenantService);
    }

    @Bean
    public TenantFeignRequestInterceptor feignTenantInterceptor() {
        return new TenantFeignRequestInterceptor();
    }

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(
            MybatisPlusInterceptor interceptor, TenantProperties tenantProperties) {
        DefaultTenantLineHandler defaultTenantLineHandler =
                new DefaultTenantLineHandler(tenantProperties.getIgnoredTables());
        TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor(defaultTenantLineHandler);
        MyBatisUtils.addInterceptor(interceptor, tenantInterceptor, 0);
        return tenantInterceptor;
    }

    @Bean
    public FilterRegistrationBean<TenantContextFilter> tenantContextFilter() {
        return WebUtils.createFilterBean(new TenantContextFilter(), TENANT_CONTEXT_FILTER);
    }

    @Bean
    public FilterRegistrationBean<TenantSecurityFilter> tenantSecurityFilter(TenantProperties tenantProperties) {
        return WebUtils.createFilterBean(
                new TenantSecurityFilter(tenantProperties.getIgnoreUrls()), TENANT_SECURITY_FILTER);
    }

    @Bean
    public MetaObjectHandler metaObjectHandler(TenantProperties tenantProperties) {
        return new TenantMetaObjectHandler(StringUtils.underlineToCamel(tenantProperties.getTenantIdColumn()));
    }

    @Bean
    @Primary
    @ConditionalOnClass(RedisCacheManager.class)
    public RedisCacheManager redisCacheManager(
            RedisTemplate<String, Object> redisTemplate,
            RedisCacheConfiguration redisCacheConfiguration,
            TenantProperties tenantProperties) {
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        return new TenantRedisCacheManager(tenantProperties.getIgnoredCaches(), cacheWriter, redisCacheConfiguration);
    }
}

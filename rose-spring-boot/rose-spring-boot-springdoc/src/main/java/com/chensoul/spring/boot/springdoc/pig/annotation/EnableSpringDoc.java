package com.chensoul.spring.boot.springdoc.pig.annotation;

import com.chensoul.spring.boot.springdoc.pig.config.OpenAPIDefinitionImportSelector;
import com.chensoul.spring.boot.springdoc.pig.config.SwaggerProperties;
import java.lang.annotation.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(SwaggerProperties.class)
@Import(OpenAPIDefinitionImportSelector.class)
public @interface EnableSpringDoc {

    /**
     * 网关路由前缀
     *
     * @return String
     */
    String value();

    /**
     * 是否是微服务架构
     *
     * @return true
     */
    boolean isMicro() default true;
}

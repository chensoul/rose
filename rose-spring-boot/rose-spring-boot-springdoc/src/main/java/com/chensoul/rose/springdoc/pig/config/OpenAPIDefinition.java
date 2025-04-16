package com.chensoul.rose.springdoc.pig.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;

/**
 * swagger配置
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class OpenAPIDefinition extends OpenAPI implements InitializingBean, ApplicationContextAware {

    @Setter
    private String path;

    private ApplicationContext applicationContext;

    private SecurityScheme securityScheme(SwaggerProperties.Security security) {
        OAuthFlow clientCredential = new OAuthFlow();
        clientCredential.setTokenUrl(security.getTokenUrl());
        clientCredential.setScopes(new Scopes().addString(security.getScope(), security.getScope()));
        OAuthFlows oauthFlows = new OAuthFlows();
        oauthFlows.password(clientCredential);
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.OAUTH2);
        securityScheme.setFlows(oauthFlows);
        return securityScheme;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SwaggerProperties swaggerProperties = applicationContext.getBean(SwaggerProperties.class);
        String appName = applicationContext.getEnvironment().getProperty("spring.application.name");

        this.info(new Info().title(StringUtils.defaultIfBlank(swaggerProperties.getTitle(), appName + " API")));

        if (swaggerProperties.getSecurity().getEnabled()) {
            // oauth2.0 password
            this.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme(swaggerProperties.getSecurity()));
        }

        // servers
        List<Server> serverList = new ArrayList<>();
        serverList.add(new Server().url(swaggerProperties.getGateway() + "/" + path));
        this.servers(serverList);
        // 支持参数平铺
        SpringDocUtils.getConfig().addSimpleTypesForParameterObject(Class.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

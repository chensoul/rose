/*
 * Copyright © 2025 Chensoul, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.security.config;

import com.chensoul.rose.security.SecurityProperties;
import com.chensoul.rose.security.rest.handler.RestAccessDeniedHandler;
import com.chensoul.rose.security.rest.handler.RestAuthenticationFailureHandler;
import com.chensoul.rose.security.rest.handler.RestAuthenticationSuccessHandler;
import com.chensoul.rose.security.rest.mfa.MfaAuthController;
import com.chensoul.rose.security.rest.mfa.MfaProperties;
import com.chensoul.rose.security.rest.provider.RestAccessAuthenticationProvider;
import com.chensoul.rose.security.rest.provider.RestLoginAuthenticationProvider;
import com.chensoul.rose.security.rest.provider.RestRefreshAuthenticationProvider;
import com.chensoul.rose.security.support.JwtTokenFactory;
import com.chensoul.rose.security.support.RestTokenFactory;
import com.chensoul.rose.security.support.TokenFactory;
import com.chensoul.rose.security.support.TransmittableSecurityContextHolderStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@AutoConfiguration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({SecurityProperties.class, MfaProperties.class})
@Import(SecurityConfig.TokenFactoryConfig.class)
@Order(org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER)
public class SecurityConfig {

    private final MfaProperties mfaProperties;

    private final UserDetailsService userDetailsService;

    private final ObjectPostProcessor<Object> objectPostProcessor;

    @Bean
    public AuthenticationManager authenticationManager(TokenFactory tokenFactory) throws Exception {
        AuthenticationManagerBuilder auth = new AuthenticationManagerBuilder(objectPostProcessor);
        DefaultAuthenticationEventPublisher eventPublisher =
                objectPostProcessor.postProcess(new DefaultAuthenticationEventPublisher());
        auth.authenticationEventPublisher(eventPublisher);

        auth.authenticationProvider(
                new RestLoginAuthenticationProvider(userDetailsService, passwordEncoder(), mfaProperties));
        auth.authenticationProvider(new RestAccessAuthenticationProvider(tokenFactory));
        auth.authenticationProvider(new RestRefreshAuthenticationProvider(userDetailsService, tokenFactory));
        return auth.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean("restAuthenticationSuccessHandler")
    public AuthenticationSuccessHandler restAuthenticationSuccessHandler(TokenFactory tokenFactory) {
        return new RestAuthenticationSuccessHandler(tokenFactory);
    }

    @Bean("restAuthenticationFailureHandler")
    public AuthenticationFailureHandler restAuthenticationFailureHandler() {
        return new RestAuthenticationFailureHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }

    /**
     * 声明调用 {@link SecurityContextHolder#setStrategyName(String)} 方法， 设置使用
     * {@link TransmittableSecurityContextHolderStrategy} 作为 Security 的上下文策略
     */
    @Bean
    public MethodInvokingFactoryBean securityContextHolderMethodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(TransmittableSecurityContextHolderStrategy.class.getName());
        return methodInvokingFactoryBean;
    }

    @Configuration
    @RequiredArgsConstructor
    public static class TokenFactoryConfig {

        private final RedisTemplate<String, Object> redisTemplate;

        private final SecurityProperties securityProperties;

        @Bean
        public TokenFactory tokenFactory() {
            if (securityProperties.getJwt().isEnabled()) {
                return new JwtTokenFactory(redisTemplate, securityProperties);
            }
            return new RestTokenFactory(redisTemplate, securityProperties);
        }
    }

    @ConditionalOnProperty(prefix = "security.jwt.mfa", value = "enabled", havingValue = "true")
    @ComponentScan(basePackageClasses = MfaAuthController.class)
    public class MfaConfig {}
}

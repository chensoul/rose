package com.chensoul.spring.boot.xxljob;

import com.chensoul.spring.boot.xxljob.core.XxlJobAutoRegister;
import com.chensoul.spring.boot.xxljob.service.JobGroupService;
import com.chensoul.spring.boot.xxljob.service.JobInfoService;
import com.chensoul.spring.boot.xxljob.service.JobLoginService;
import com.chensoul.spring.boot.xxljob.service.impl.JobGroupServiceImpl;
import com.chensoul.spring.boot.xxljob.service.impl.JobInfoServiceImpl;
import com.chensoul.spring.boot.xxljob.service.impl.JobLoginServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(RestTemplate.class)
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnProperty(prefix = "xxl.xxljob.client", name = "enabled", havingValue = "true")
public class XxlJobClientConfiguration {

    private final RestTemplate restTemplate;

    private final XxlJobProperties xxlJobProperties;

    private final Environment environment;

    @Bean
    public JobLoginService jobLoginService() {
        return new JobLoginServiceImpl(restTemplate, xxlJobProperties);
    }

    @Bean
    public JobGroupService jobGroupService() {
        return new JobGroupServiceImpl(
                jobLoginService(), restTemplate, xxlJobProperties.getAdmin().getAddresses());
    }

    @Bean
    public JobInfoService jobInfoService() {
        return new JobInfoServiceImpl(jobLoginService(), restTemplate, xxlJobProperties);
    }

    @Bean
    public XxlJobAutoRegister xxlJobAutoRegister() {
        log.info("Initializing XxlJobAutoRegister");

        return new XxlJobAutoRegister(jobGroupService(), jobInfoService(), xxlJobProperties, environment);
    }
}

# chensoul-cloud

## TODO

- [ ] 升级 nacos 版本到 2.5.1
- [ ] 使用 Redis + Lua 基于令牌桶实现限流
- [ ] 通过分布式事务 Seata 保证告警、整改和任务的状态一致性
- [ ] 使用 Spring Security OAuth2 实现用户认证和授权
- [ ] 使用限流 + 队列保证回调接口（供三方系统使用的）的可靠性，使用 Redis + MySQL 实现去重
- [ ] 使用 AOP + Redis 防止重复提交
- [ ] 使用 SkyWalking + Prometheus + Grafana 监控服务
- [ ] 使用 Sentinel + OpenFeign 实现熔断、降级、限流

## Tech stack

| Tech stack           | Version    | Latest Version                                                                                                                                                                                                               | Notes |
|----------------------|------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------|
| Spring Boot          | 2.7.18     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/org/springframework/boot/spring-boot-dependencies/maven-metadata.xml">                        |       |
| Spring Cloud         | 2021.0.9   | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021&metadataUrl=https://repo1.maven.org/maven2/org/springframework/cloud/spring-cloud-dependencies/maven-metadata.xml">                   |       |
| Spring Cloud Alibaba | 2021.0.6.2 | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2021.0&metadataUrl=https://repo1.maven.org/maven2/com/alibaba/cloud/spring-cloud-alibaba-dependencies/maven-metadata.xml">                 |       |
| Spring Authorization | 0.4.5      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=0&metadataUrl=https://repo1.maven.org/maven2/org/springframework/security/spring-security-oauth2-authorization-server/maven-metadata.xml"> |       |
| Spring Boot Admin    | 2.7.16     | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=2&metadataUrl=https://repo1.maven.org/maven2/de/codecentric/spring-boot-admin-dependencies/maven-metadata.xml">                            |       |
| MyBatis Plus	        | 3.5.7      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=3&metadataUrl=https://repo1.maven.org/maven2/com/baomidou/mybatis-plus-bom/maven-metadata.xml">                                            |       |
| SpringDoc OpenAPI    | 1.8.0      | <img src="https://img.shields.io/maven-metadata/v?label=&color=blue&versionPrefix=1&metadataUrl=https://repo1.maven.org/maven2/org/springdoc/springdoc-openapi/maven-metadata.xml">                                          
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
package com.chensoul.rose.oss.old.storage.properties;

import static com.chensoul.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_MINIO;

import java.time.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * minio 配置信息
 *
 * @author Levin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = OSS_CONFIG_PREFIX_MINIO)
public class MinioOssProperties extends BaseOssProperties {

    /**
     * minio实例的URL。包括端口。如果未提供端口，则采用HTTP端口。
     */
    private String endpoint = "https://play.min.io";

    private int port = 80;

    private String region;

    /**
     * minio实例上的访问
     */
    private String accessKey = "Q3AM3UQ867SPQQA43P2F";

    /**
     * minio实例上的密钥（密码）
     */
    private String secretKey = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";

    /**
     * If the scheme is not provided in {@code url} property, define if the connection is
     * done via HTTP or HTTPS.
     */
    private Boolean secure = false;

    /**
     * 在执行器上注册的度量配置前缀。
     */
    private String metricName = "minio.oss";

    /**
     * 连接超时时间。
     */
    private Duration connectTimeout = Duration.ofSeconds(10);

    /**
     * 写入超时时间
     */
    private Duration writeTimeout = Duration.ofSeconds(60);

    /**
     * 读取超时时间
     */
    private Duration readTimeout = Duration.ofSeconds(10);
}

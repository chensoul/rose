/*
 * Copyright (c) 2023 WEMIRR-PLATFORM Authors. All Rights Reserved.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chensoul.rose.oss.old.storage.cloud.aliyun;

import static com.chensoul.rose.oss.old.storage.OssOperation.ALI_YUN_OSS_OPERATION;
import static com.chensoul.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_ALIYUN;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.chensoul.rose.oss.old.storage.AliYunOssOperation;
import com.chensoul.rose.oss.old.storage.properties.AliYunOssProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 OSS
 *
 * @author Levin
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(AliYunOssProperties.class)
@ConditionalOnProperty(prefix = OSS_CONFIG_PREFIX_ALIYUN, name = "enabled", havingValue = "true")
public class AliYunOssAutoConfiguration {

    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(AliYunOssProperties properties) {
        return new OSSClientBuilder()
                .build(properties.getEndpoint(), properties.getAccessKey(), properties.getSecretKey());
    }

    @Bean(ALI_YUN_OSS_OPERATION)
    public AliYunOssOperation aliYunStorageOperation(OSS ossClient, AliYunOssProperties properties) {
        return new AliYunOssOperation(ossClient, properties);
    }
}

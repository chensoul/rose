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

import static com.chensoul.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_ALIYUN;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Levin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = OSS_CONFIG_PREFIX_ALIYUN)
public class AliYunOssProperties extends BaseOssProperties {

    /**
     * endpoint
     */
    private String endpoint = "http://cloud.aliyuncs.com";
}

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

import static com.chensoul.rose.oss.old.storage.OssOperation.OSS_CONFIG_PREFIX_TENCENT;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Levin
 * @since 2018-09-17 11:09
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = OSS_CONFIG_PREFIX_TENCENT)
public class TencentOssProperties extends BaseOssProperties {

    /**
     * AppId
     */
    private String appId;

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 绑定的域名
     */
    private String domain;

    /**
     * 所属地区
     */
    private String region;
}

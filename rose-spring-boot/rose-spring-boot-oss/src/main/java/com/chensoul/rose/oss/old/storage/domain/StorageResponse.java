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
package com.chensoul.rose.oss.old.storage.domain;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 响应结果
 *
 * @author Levin
 */
@Data
@NoArgsConstructor
public class StorageResponse implements java.io.Serializable {

    private static final String SEPARATOR = "/";

    private Object fileId;

    private String etag;

    private String originName;

    private String targetName;

    private long size;

    /**
     * 文件存储的名字
     */
    private String md5;

    /**
     * 文件的完整路径
     */
    private String fullUrl;

    private String mappingPath;

    private String bucket;

    /**
     * 对应存储的扩展字段
     */
    private Map<String, Object> extend;

    @Builder
    public StorageResponse(
            String fileId,
            String etag,
            String originName,
            String targetName,
            String mappingPath,
            String bucket,
            long size,
            String md5,
            String fullUrl,
            Map<String, Object> extend) {
        this.fileId = fileId;
        this.etag = etag;
        this.originName = originName;
        this.targetName = targetName;
        this.size = size;
        this.md5 = md5;
        this.bucket = bucket;
        this.extend = extend;
        this.mappingPath = mappingPath;
        if (StringUtils.isBlank(fullUrl)) {
            this.fullUrl = buildFullUrl(mappingPath, targetName);
        } else {
            this.fullUrl = fullUrl;
        }
    }

    public static String buildFullUrl(String mappingPath, String targetName) {
        if (mappingPath.endsWith(SEPARATOR) && targetName.startsWith(SEPARATOR)) {
            mappingPath = mappingPath.substring(0, mappingPath.length() - 1);
        }
        return StringUtils.join(mappingPath, targetName);
    }
}

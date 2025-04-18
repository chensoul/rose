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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应结果
 *
 * @author Levin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DownloadResponse {

    @Deprecated
    private BufferedReader bufferedReader;

    private InputStream inputStream;

    /**
     * 上下文类型
     */
    private String contentType;

    /**
     * 内容编码
     */
    private String contentEncoding;

    /**
     * 文件长度
     */
    private long contentLength;

    /**
     * 文件-本地
     */
    private File file;

    /**
     * 本地文件地址
     */
    private String localFilePath;
}

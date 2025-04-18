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
package com.chensoul.rose.oss.model;

import java.util.Date;
import lombok.Data;

/**
 * OssFile
 *
 * @author Chill
 */
@Data
public class OssFile {

    /**
     * 文件hash值
     */
    public String hash;

    /**
     * 文件地址
     */
    private String link;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小
     */
    private long length;

    /**
     * 文件上传时间
     */
    private Date putTime;

    /**
     * 文件contentType
     */
    private String contentType;
}

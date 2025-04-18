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
package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.rose.mybatis.model.TenantEntity;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_log")
public class Log extends TenantEntity implements Serializable {

    private static final long serialVersionUID = 1129753896999673095L;

    private String name;

    private String serverIp;

    private String clientIp;

    private String userAgent;

    private String requestUrl;

    private String requestParams;

    private String requestMethod;

    private Long costTime;

    private boolean success;

    private String exception;

    private String traceId;
}

/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chensoul.rose.upms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.rose.mybatis.model.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_dept")
public class Department extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父主键
     */
    private Long parentId;

    /**
     * 祖级机构主键
     */
    private String traceId;

    /**
     * 部门名
     */
    private String name;

    /**
     * 部门全称
     */
    private String fullName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String description;

    private Integer status;
}

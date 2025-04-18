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
package com.chensoul.rose.core.util;

import com.chensoul.rose.core.exception.ResultCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 统一封装 Restful 接口返回信息
 * <p>
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RestResponse<T> implements Serializable {
    private int code;
    private String message;
    private T data;

    public static RestResponse<Void> ok() {
        return ok(null);
    }

    public static <T> RestResponse<T> ok(T data) {
        return ok(data, ResultCode.SUCCESS.getName());
    }

    public static <T> RestResponse<T> ok(T data, String message) {
        return build(data, ResultCode.SUCCESS.getCode(), message);
    }

    public static <T> RestResponse<T> error() {
        return error(null, ResultCode.INTERNAL_ERROR.getName());
    }

    public static <T> RestResponse<T> error(String message) {
        return error(null, message);
    }

    public static <T> RestResponse<T> error(T data) {
        return error(data, ResultCode.INTERNAL_ERROR.getName());
    }

    public static <T> RestResponse<T> error(T data, String message) {
        return build(data, ResultCode.INTERNAL_ERROR.getCode(), message);
    }

    public static <T> RestResponse<T> build(T data, int code, String message) {
        RestResponse<T> restResponse = new RestResponse<>();
        restResponse.code = code;
        restResponse.message = message;
        restResponse.data = data;
        return restResponse;
    }

    @JsonIgnore
    public Boolean isSuccess() {
        return (this.code == ResultCode.SUCCESS.getCode());
    }

    @JsonIgnore
    public Map<String, Object> toMap() {
        return Maps.of("code", code, "message", message, "data", data);
    }
}

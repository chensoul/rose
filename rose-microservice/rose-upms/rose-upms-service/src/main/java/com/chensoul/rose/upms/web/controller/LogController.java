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
package com.chensoul.rose.upms.web.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chensoul.rose.core.util.RestResponse;
import com.chensoul.rose.upms.domain.entity.Log;
import com.chensoul.rose.upms.domain.model.LogAddRequest;
import com.chensoul.rose.upms.domain.service.LogService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @GetMapping
    public RestResponse<IPage<Log>> page(Page<Log> page, LogAddRequest logAddRequest) {
        return RestResponse.ok(logService.page(
                page,
                Wrappers.<Log>lambdaQuery()
                        .ge(
                                Objects.nonNull(logAddRequest.getBeginTime()),
                                Log::getCreateTime,
                                logAddRequest.getBeginTime())
                        .le(
                                Objects.nonNull(logAddRequest.getEndTime()),
                                Log::getCreateTime,
                                logAddRequest.getEndTime())));
    }

    @PostMapping
    public RestResponse<Void> saveLog(@RequestBody Log log) {
        logService.save(log);
        return RestResponse.ok();
    }
}

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

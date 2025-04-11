package com.chensoul.upms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chensoul.core.util.RestResponse;
import com.chensoul.upms.entity.Log;
import com.chensoul.upms.model.param.LogParam;
import com.chensoul.upms.service.LogService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @GetMapping
    public RestResponse<IPage<Log>> page(Page<Log> page, LogParam logParam) {
        return RestResponse.ok(logService.page(
                page,
                Wrappers.<Log>lambdaQuery()
                        .ge(Objects.nonNull(logParam.getBeginTime()), Log::getCreateTime, logParam.getBeginTime())
                        .le(Objects.nonNull(logParam.getEndTime()), Log::getCreateTime, logParam.getEndTime())));
    }

    @PostMapping
    public RestResponse<Void> saveLog(@RequestBody Log log) {
        logService.save(log);
        return RestResponse.ok();
    }
}

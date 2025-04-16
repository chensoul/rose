package com.chensoul.rose.upms.client;

import com.chensoul.rose.core.util.RestResponse;
import com.chensoul.rose.upms.ServiceNameConstants;
import com.chensoul.rose.upms.domain.entity.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteLogService {

    @PostMapping("/log")
    RestResponse<Boolean> saveLog(@RequestBody Log log);
}

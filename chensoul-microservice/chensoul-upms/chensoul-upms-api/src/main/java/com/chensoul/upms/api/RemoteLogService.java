package com.chensoul.upms.api;

import com.chensoul.core.util.RestResponse;
import com.chensoul.upms.ServiceNameConstants;
import com.chensoul.upms.entity.Log;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteLogService {

	@PostMapping("/log")
	RestResponse<Boolean> saveLog(@RequestBody Log log);

}

package com.chensoul.rose.upms.client;

// import com.chensoul.support.util.RestResponse;

import com.chensoul.rose.core.util.RestResponse;
import com.chensoul.rose.upms.ServiceNameConstants;
import com.chensoul.rose.upms.domain.entity.OAuth2Client;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = ServiceNameConstants.SYSTEM_SERVICE)
public interface RemoteClientService {

    @GetMapping("/OAuth2Client/list")
    RestResponse<List<OAuth2Client>> getClientList(@SpringQueryMap OAuth2Client OAuth2Client);

    @GetMapping("/client/getClientByCode/{code}")
    RestResponse<OAuth2Client> getClientByCode(@PathVariable("code") String code);
}

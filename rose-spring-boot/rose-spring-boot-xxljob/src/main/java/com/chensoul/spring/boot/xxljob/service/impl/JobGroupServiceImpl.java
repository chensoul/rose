package com.chensoul.spring.boot.xxljob.service.impl;

import com.chensoul.core.exception.BusinessException;
import com.chensoul.spring.boot.xxljob.model.XxlJobGroup;
import com.chensoul.spring.boot.xxljob.model.XxlJobGroupPage;
import com.chensoul.spring.boot.xxljob.model.XxlRestResponse;
import com.chensoul.spring.boot.xxljob.service.JobGroupService;
import com.chensoul.spring.boot.xxljob.service.JobLoginService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class JobGroupServiceImpl implements JobGroupService {

    private final JobLoginService jobLoginService;

    private final RestTemplate restTemplate;

    private final String host;

    @Override
    public List<XxlJobGroup> getJobGroup(String appName) {
        String url = host + "/jobgroup/pageList";

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("appname", appName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Cookie", jobLoginService.getCookie());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<XxlJobGroupPage> response =
                restTemplate.postForEntity(url, requestEntity, XxlJobGroupPage.class);
        List<XxlJobGroup> jobGroup = response.getBody().getData();

        return jobGroup.stream()
                .filter(xxlJobGroup -> xxlJobGroup.getAppname().equals(appName))
                .collect(Collectors.toList());
    }

    @Override
    public void autoRegisterGroup(String appName, String title) {
        String url = host + "/jobgroup/save";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("appname", appName);
        map.add("title", appName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie", jobLoginService.getCookie());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        ResponseEntity<XxlRestResponse> response =
                restTemplate.postForEntity(url, requestEntity, XxlRestResponse.class);

        XxlRestResponse xxlRestResponse = response.getBody();
        if (xxlRestResponse.getCode() != 200) {
            throw new BusinessException(xxlRestResponse.getMsg());
        }
    }
}

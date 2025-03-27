package com.chensoul.spring.boot.xxljob.service.impl;

import com.chensoul.core.exception.BusinessException;
import com.chensoul.spring.boot.xxljob.XxlJobProperties;
import com.chensoul.spring.boot.xxljob.model.XxlJobInfo;
import com.chensoul.spring.boot.xxljob.model.XxlJobInfoPage;
import com.chensoul.spring.boot.xxljob.model.XxlRestResponse;
import com.chensoul.spring.boot.xxljob.service.JobInfoService;
import com.chensoul.spring.boot.xxljob.service.JobLoginService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

@RequiredArgsConstructor
public class JobInfoServiceImpl implements JobInfoService {
	private final JobLoginService jobLoginService;
	private final RestTemplate restTemplate;
	private final XxlJobProperties xxlJobProperties;

	@SneakyThrows
	public static MultiValueMap<String, Object> convertObjectToMultiValueMap(Object obj) {
		MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Object fieldValue = field.get(obj);
			if (fieldValue != null) {
				multiValueMap.add(fieldName, fieldValue.toString());
			}
		}
		return multiValueMap;
	}

	@Override
	public List<XxlJobInfo> listJob(Integer jobGroupId, String executorHandler) {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/jobinfo/pageList";

		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("jobGroup", jobGroupId);
		map.add("executorHandler", executorHandler);
		map.add("triggerStatus", -1);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Cookie", jobLoginService.getCookie());

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<XxlJobInfoPage> response = restTemplate.postForEntity(url, requestEntity, XxlJobInfoPage.class);
		return response.getBody().getData();
	}

	@Override
	public Integer addJob(XxlJobInfo xxlJobInfo) {
		return Integer.valueOf(executeAction("add", convertObjectToMultiValueMap(xxlJobInfo)).getContent());
	}

	@Override
	public void updateJob(XxlJobInfo xxlJobInfo) {
		executeAction("update", convertObjectToMultiValueMap(xxlJobInfo));
	}

	@Override
	public void removeJob(Integer jobId) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("id", jobId);
		executeAction("remove", params);
	}

	@Override
	public void startJob(Integer jobId) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("id", jobId);
		executeAction("start", params);
	}

	@Override
	public void stopJob(Integer jobId) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("id", jobId);
		executeAction("stop", params);
	}

	private XxlRestResponse executeAction(String action, MultiValueMap<String, Object> params) {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/jobinfo/" + action;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Cookie", jobLoginService.getCookie());

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
		ResponseEntity<XxlRestResponse> response = restTemplate.postForEntity(url, requestEntity, XxlRestResponse.class);

		XxlRestResponse xxlRestResponse = response.getBody();
		if (xxlRestResponse.getCode() != 200) {
			throw new BusinessException(xxlRestResponse.getMsg());
		}
		return xxlRestResponse;
	}
}

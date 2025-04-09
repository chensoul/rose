package com.chensoul.spring.boot.xxljob.service.impl;

import com.chensoul.spring.boot.xxljob.XxlJobProperties;
import com.chensoul.spring.boot.xxljob.service.JobLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class JobLoginServiceImpl implements JobLoginService {

	private final Map<String, String> loginCookie = new HashMap<>();

	private final RestTemplate restTemplate;

	private final XxlJobProperties xxlJobProperties;

	public static String getCookieValue(String cookieStr, String cookieName) {
		String prefix = cookieName + "=";
		int index = cookieStr.indexOf(prefix);
		if (index != -1) {
			int start = index + prefix.length();
			int end = cookieStr.indexOf(";", start);
			if (end == -1) {
				end = cookieStr.length();
			}
			return cookieStr.substring(start, end);
		}
		return null;
	}

	@Override
	public void login() {
		String url = xxlJobProperties.getAdmin().getAddresses() + "/login";

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("userName", xxlJobProperties.getClient().getUsername());
		map.add("password", xxlJobProperties.getClient().getPassword());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
		List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
		loginCookie.put("XXL_JOB_LOGIN_IDENTITY", getCookieValue(cookies.get(0), "XXL_JOB_LOGIN_IDENTITY"));
	}

	@Override
	public String getCookie() {
		for (int i = 0; i < 3; i++) {
			String cookieStr = loginCookie.get("XXL_JOB_LOGIN_IDENTITY");
			if (cookieStr != null) {
				return "XXL_JOB_LOGIN_IDENTITY=" + cookieStr;
			}
			login();
		}
		throw new RuntimeException("xxl-job登录失败，请检查设置");
	}

}

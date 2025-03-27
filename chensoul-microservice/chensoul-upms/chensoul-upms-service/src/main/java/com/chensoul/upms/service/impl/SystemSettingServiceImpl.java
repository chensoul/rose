package com.chensoul.upms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.upms.entity.SystemSetting;
import com.chensoul.upms.mapper.SystemSettingMapper;
import com.chensoul.upms.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl extends ServiceImpl<SystemSettingMapper, SystemSetting> implements SystemSettingService {
	private final HttpServletRequest request;

	@Override
	public String getBaseUrl() {
		return "";
	}
}

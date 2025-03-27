package com.chensoul.upms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chensoul.upms.entity.SystemSetting;

public interface SystemSettingService extends IService<SystemSetting> {

	String getBaseUrl();
}

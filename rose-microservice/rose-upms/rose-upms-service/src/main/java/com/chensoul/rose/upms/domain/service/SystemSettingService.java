package com.chensoul.rose.upms.domain.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chensoul.rose.upms.domain.entity.SystemSetting;

public interface SystemSettingService extends IService<SystemSetting> {

    String getBaseUrl();
}

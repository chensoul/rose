package com.chensoul.rose.upms.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chensoul.rose.upms.domain.entity.SystemSetting;
import com.chensoul.rose.upms.domain.mapper.SystemSettingMapper;
import com.chensoul.rose.upms.domain.service.SystemSettingService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl extends ServiceImpl<SystemSettingMapper, SystemSetting>
        implements SystemSettingService {

    private final HttpServletRequest request;

    @Override
    public String getBaseUrl() {
        return "";
    }
}

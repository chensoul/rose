/*
 * Copyright © 2025 Chensoul, Inc. (ichensoul@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

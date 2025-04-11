package com.chensoul.spring.boot.xxljob.service;

import com.chensoul.spring.boot.xxljob.model.XxlJobGroup;
import java.util.List;

public interface JobGroupService {

    List<XxlJobGroup> getJobGroup(String appName);

    void autoRegisterGroup(String appName, String title);
}

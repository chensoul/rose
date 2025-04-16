package com.chensoul.rose.xxljob.service;

import com.chensoul.rose.xxljob.model.XxlJobGroup;
import java.util.List;

public interface JobGroupService {

    List<XxlJobGroup> getJobGroup(String appName);

    void autoRegisterGroup(String appName, String title);
}

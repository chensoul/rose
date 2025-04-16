package com.chensoul.rose.xxljob.service;

import com.chensoul.rose.xxljob.model.XxlJobInfo;
import java.util.List;

public interface JobInfoService {

    List<XxlJobInfo> listJob(Integer jobGroupId, String executorHandler);

    Integer addJob(XxlJobInfo xxlJobInfo);

    void updateJob(XxlJobInfo xxlJobInfo);

    void removeJob(Integer jobId);

    void startJob(Integer jobId);

    void stopJob(Integer jobId);
}

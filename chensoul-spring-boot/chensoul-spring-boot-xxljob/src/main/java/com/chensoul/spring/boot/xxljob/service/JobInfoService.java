package com.chensoul.spring.boot.xxljob.service;

import com.chensoul.spring.boot.xxljob.model.XxlJobInfo;

import java.util.List;

public interface JobInfoService {

	List<XxlJobInfo> listJob(Integer jobGroupId, String executorHandler);

	Integer addJob(XxlJobInfo xxlJobInfo);

	void updateJob(XxlJobInfo xxlJobInfo);

	void removeJob(Integer jobId);

	void startJob(Integer jobId);

	void stopJob(Integer jobId);
}

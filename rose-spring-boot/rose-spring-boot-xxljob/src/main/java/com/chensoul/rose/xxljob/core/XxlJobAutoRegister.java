package com.chensoul.rose.xxljob.core;

import com.chensoul.rose.core.exception.BusinessException;
import com.chensoul.rose.core.spring.SpringContextHolder;
import com.chensoul.rose.xxljob.anntation.XxlRegister;
import com.chensoul.rose.xxljob.config.XxlJobProperties;
import com.chensoul.rose.xxljob.model.XxlJobGroup;
import com.chensoul.rose.xxljob.model.XxlJobInfo;
import com.chensoul.rose.xxljob.service.JobGroupService;
import com.chensoul.rose.xxljob.service.JobInfoService;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

@Slf4j
@RequiredArgsConstructor
public class XxlJobAutoRegister implements ApplicationListener<ApplicationReadyEvent>, ApplicationContextAware {

    private final JobGroupService jobGroupService;

    private final JobInfoService jobInfoService;

    private final XxlJobProperties xxlJobProperties;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String appName = SpringContextHolder.getApplicationName();

        List<XxlJobGroup> jobGroups = jobGroupService.getJobGroup(appName);
        if (CollectionUtils.isEmpty(jobGroups)) {
            throw new BusinessException("执行器管理" + appName + "没有注册");
        }
        XxlJobGroup xxlJobGroup = jobGroups.get(0);
        addJobInfo(xxlJobGroup);
    }

    private void addJobGroup(String appName) {
        if (CollectionUtils.isEmpty(jobGroupService.getJobGroup(appName))) {
            jobGroupService.autoRegisterGroup(appName, appName);
        }
    }

    private void addJobInfo(XxlJobGroup xxlJobGroup) {
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, XxlJob> annotatedMethods =
                    MethodIntrospector.selectMethods(bean.getClass(), (MethodIntrospector.MetadataLookup<XxlJob>)
                            method -> AnnotatedElementUtils.findMergedAnnotation(method, XxlJob.class));
            for (Map.Entry<Method, XxlJob> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                XxlJob xxlJob = methodXxlJobEntry.getValue();

                if (executeMethod.isAnnotationPresent(XxlRegister.class)) {
                    XxlRegister xxlRegister = executeMethod.getAnnotation(XxlRegister.class);
                    List<XxlJobInfo> jobInfo = jobInfoService.listJob(xxlJobGroup.getId(), xxlJob.value());
                    if (!jobInfo.isEmpty()) {
                        // 服务端是模糊查询，需要再判断一次
                        Optional<XxlJobInfo> first = jobInfo.stream()
                                .filter(xxlJobInfo ->
                                        xxlJobInfo.getExecutorHandler().equals(xxlJob.value()))
                                .findFirst();
                        if (first.isPresent()) {
                            continue;
                        }
                    }

                    XxlJobInfo xxlJobInfo = createXxlJobInfo(xxlJobGroup, xxlJob, xxlRegister);
                    Integer jobId = jobInfoService.addJob(xxlJobInfo);
                    log.info("Auto register xxljob {} success, jobId: {}", xxlRegister.jobDesc(), jobId);
                }
            }
        }
    }

    private XxlJobInfo createXxlJobInfo(XxlJobGroup xxlJobGroup, XxlJob xxlJob, XxlRegister xxlRegister) {
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        xxlJobInfo.setJobGroup(xxlJobGroup.getId());
        xxlJobInfo.setJobDesc(StringUtils.defaultIfBlank(xxlRegister.jobDesc(), xxlJob.value() + " 任务"));
        xxlJobInfo.setAuthor(StringUtils.defaultIfBlank(
                xxlRegister.author(), xxlJobProperties.getClient().getAuthor()));
        xxlJobInfo.setAlarmEmail(StringUtils.defaultIfBlank(
                xxlRegister.alarmEmail(), xxlJobProperties.getClient().getAlarmEmail()));
        xxlJobInfo.setScheduleType(xxlJobProperties.getClient().getScheduleType());
        xxlJobInfo.setScheduleConf(xxlRegister.cron());
        xxlJobInfo.setGlueType(xxlJobProperties.getClient().getGlueType());
        xxlJobInfo.setExecutorHandler(xxlJob.value());
        xxlJobInfo.setExecutorParam(xxlRegister.executorParam());
        xxlJobInfo.setExecutorRouteStrategy(StringUtils.defaultIfBlank(
                xxlRegister.executorRouteStrategy(),
                xxlJobProperties.getClient().getExecutorRouteStrategy()));
        xxlJobInfo.setMisfireStrategy(xxlJobProperties.getClient().getMisfireStrategy());
        xxlJobInfo.setExecutorBlockStrategy(xxlJobProperties.getClient().getExecutorBlockStrategy());
        xxlJobInfo.setExecutorTimeout(xxlJobProperties.getClient().getExecutorTimeout());
        xxlJobInfo.setExecutorFailRetryCount(xxlJobProperties.getClient().getExecutorFailRetryCount());
        xxlJobInfo.setTriggerStatus(xxlRegister.autoStart() ? 1 : 0);

        return xxlJobInfo;
    }
}

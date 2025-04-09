package com.chensoul.spring.boot.xxljob;

import com.alibaba.ttl.TtlRunnable;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableAsync
@ConditionalOnClass(XxlJobSpringExecutor.class)
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobExecutorConfiguration {

	private static String logBasePath = "/data/logs/xxl-xxljob/";

	// private static final String XXL_JOB_ADMIN = "chensoul-xxljob";
	// private DiscoveryClient discoveryClient

	@Bean
	public BeanPostProcessor threadPoolTaskExecutorBeanPostProcessor() {
		return new BeanPostProcessor() {

			@Override
			public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
				if (!(bean instanceof ThreadPoolTaskExecutor)) {
					return bean;
				}
				// 修改提交的任务，接入 TransmittableThreadLocal
				ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) bean;
				executor.setTaskDecorator(TtlRunnable::get);
				return executor;
			}

		};
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "xxl.job.executor", name = "enabled", havingValue = "true", matchIfMissing = true)
	public XxlJobExecutor xxlJobExecutor(XxlJobProperties properties, Environment environment) {
		log.info("Initializing XxlJobExecutor");

		XxlJobProperties.AdminProperties admin = properties.getAdmin();
		XxlJobProperties.ExecutorProperties executor = properties.getExecutor();
		String appName = environment.getProperty("spring.application.name");

		XxlJobExecutor xxlJobExecutor = new XxlJobSpringExecutor();
		xxlJobExecutor.setIp(executor.getIp());
		xxlJobExecutor.setPort(executor.getPort());
		xxlJobExecutor.setAppname(StringUtils.defaultString(executor.getAppName(), appName));
		xxlJobExecutor.setLogPath(StringUtils.defaultString(executor.getLogPath(), logBasePath + appName));
		xxlJobExecutor.setLogRetentionDays(executor.getLogRetentionDays());
		xxlJobExecutor.setAccessToken(properties.getAccessToken());

		// if (StringUtils.isBlank(admin.getAddresses())) {
		// String serverList = discoveryClient.getServices().stream().filter(s ->
		// s.contains(XXL_JOB_ADMIN))
		// .flatMap(s -> discoveryClient.getInstances(s).stream()).map(instance ->
		// String.format("http://%s:%s/%s", instance.getHost(), instance.getPort(),
		// "xxl-xxljob-admin))
		// .collect(Collectors.joining(","));
		// xxlJobExecutor.setAdminAddresses(serverList);
		// } else {
		xxlJobExecutor.setAdminAddresses(properties.getAdmin().getAddresses());
		// }
		return xxlJobExecutor;
	}

}

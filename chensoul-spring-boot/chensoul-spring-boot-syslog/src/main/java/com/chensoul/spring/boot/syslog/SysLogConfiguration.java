package com.chensoul.spring.boot.syslog;

import com.chensoul.spring.boot.syslog.aspect.SysLogAspect;
import com.chensoul.spring.boot.syslog.event.SysLogListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 日志自动配置
 */
@Slf4j
@EnableAsync
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
public class SysLogConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SysLogListener sysLogListener() {
		return new SysLogListener(sysLogInfo -> {
			log.info("sysLogInfo: {}", sysLogInfo);
		});
	}

	@Bean
	public SysLogAspect sysLogAspect() {
		log.info("Initializing SysLogAspect");
		return new SysLogAspect();
	}

}

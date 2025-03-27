package com.chensoul.spring.boot.springdoc.util;

import com.chensoul.core.util.LauncherService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import java.util.Properties;

public class SpringdocLauncherService implements LauncherService {
	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		Properties props = System.getProperties();
		props.setProperty("spring.mvc.pathmatch.matching-handler", "ANT_PATH_MATCHER");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}

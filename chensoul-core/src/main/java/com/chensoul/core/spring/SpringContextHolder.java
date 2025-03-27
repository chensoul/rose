package com.chensoul.core.spring;

import com.chensoul.core.util.StringPool;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
	@Getter
	private static ApplicationContext applicationContext;

	private SpringContextHolder() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return applicationContext.getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return applicationContext.getBeanNamesForType(type);
	}

	public static String getProperty(String key) {
		return applicationContext.getEnvironment().getProperty(key);
	}

	public static String getApplicationName() {
		return applicationContext.getEnvironment().getProperty("spring.application.name", String.class, StringPool.DASHDASH);
	}

	public static Set<String> getActiveProfiles() {
		return Arrays.stream(applicationContext.getEnvironment().getActiveProfiles()).collect(Collectors.toSet());
	}

	public static void publishEvent(ApplicationEvent event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

	public static void publishEvent(Object event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

	public static boolean isMicro() {
		return applicationContext.getEnvironment().getProperty("spring.cloud.nacos.discovery.enabled", Boolean.class, true);
	}

	public static void clearHolder() {
		if (log.isDebugEnabled()) {
			log.debug("clear applicationContext: {}", applicationContext);
		}
		applicationContext = null;
	}

	@Override
	@SneakyThrows
	public void destroy() {
		SpringContextHolder.clearHolder();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextHolder.applicationContext = applicationContext;
	}
}

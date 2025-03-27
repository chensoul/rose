package com.chensoul.spring.boot.redis.mircometer;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ReflectionUtils;

/**
 * {@link RedisTemplate} {@link BeanPostProcessor} 实现 - 拦截并且处理 RedisTemplate
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 1.0.0
 */
public class RedisTemplateBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

	private ObjectProvider<RedisOperationInterceptor> interceptors;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> beanType = AopUtils.getTargetClass(bean);
		if (RedisTemplate.class.isAssignableFrom(beanType)) {
			RedisTemplate<?, ?> redisTemplate = (RedisTemplate<?, ?>) bean;
			enhanceValueOperations(redisTemplate);
		}
		return bean;
	}

	private void enhanceValueOperations(RedisTemplate<?, ?> redisTemplate) {
		ReflectionUtils.doWithFields(RedisTemplate.class, field -> {
				field.setAccessible(true);
				// 代理对象
				ValueOperations<?, ?> delegate = (ValueOperations<?, ?>) field.get(redisTemplate);
				ValueOperationsWrapper wrapper = new ValueOperationsWrapper(delegate, interceptors);
				// 设置 Wrapper 成为 valueOps 字段内容
				field.set(redisTemplate, wrapper);
			},
			field -> "valueOps".equals(field.getName()) && ValueOperations.class.equals(field.getType())
		);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.interceptors = beanFactory.getBeanProvider(RedisOperationInterceptor.class);
	}
}

package com.chensoul.spring.boot.redis;

import com.chensoul.core.jackson.JacksonUtils;
import com.chensoul.spring.boot.redis.mircometer.MetricsRedisOperationInterceptor;
import com.chensoul.spring.boot.redis.mircometer.RedisTemplateBeanPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
@AutoConfigureBefore({ RedisConfiguration.class, RedisAutoConfiguration.class })
@EnableConfigurationProperties({ CacheProperties.class })
public class RedisTemplateConfiguration {

	public RedisTemplateConfiguration() {
		log.info("Initializing RedisTemplateConfiguration");
	}

	@Bean
	@ConditionalOnMissingBean(RedisSerializer.class)
	public RedisSerializer<Object> redisSerializer() {
		return new GenericJackson2JsonRedisSerializer(JacksonUtils.OBJECT_MAPPER);
	}

	@Bean
	@ConditionalOnClass(RedisOperations.class)
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		redisTemplate.setKeySerializer(RedisSerializer.json());
		redisTemplate.setValueSerializer(redisSerializer());

		redisTemplate.setHashKeySerializer(RedisSerializer.json());
		redisTemplate.setHashValueSerializer(redisSerializer());
		redisTemplate.afterPropertiesSet();

		return redisTemplate;
	}

	@Bean
	public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	@Bean
	public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	@Bean
	public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

	@Bean
	public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	@Bean
	public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForZSet();
	}

	@Bean
	public DefaultRedisScript<Long> redisScript() {
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptText(redisScriptText());
		redisScript.setResultType(Long.class);
		return redisScript;
	}

	private String redisScriptText() {
		return "local key = KEYS[1]\n" + "local count = tonumber(ARGV[1])\n" + "local time = tonumber(ARGV[2])\n"
				+ "local current = mq.call('get', key);\n" + "if current and tonumber(current) > count then\n"
				+ "    return tonumber(current);\n" + "end\n" + "current = mq.call('incr', key)\n"
				+ "if tonumber(current) == 1 then\n" + "    mq.call('expire', key, time)\n" + "end\n"
				+ "return tonumber(current);";
	}

	@Bean
	public RedisTemplateBeanPostProcessor redisTemplateBeanPostProcessor() {
		return new RedisTemplateBeanPostProcessor();
	}

	@Bean
	public MetricsRedisOperationInterceptor redisOperationMetricsInterceptor() {
		return new MetricsRedisOperationInterceptor();
	}

}

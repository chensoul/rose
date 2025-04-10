package com.chensoul.spring.boot.redis;

import com.chensoul.spring.boot.redis.service.RedisService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
@AutoConfigureBefore({RedisConfiguration.class, RedisAutoConfiguration.class})
@EnableConfigurationProperties({CacheProperties.class})
public class RedisTemplateConfiguration {

    public RedisTemplateConfiguration() {
        log.info("Initializing RedisTemplateConfiguration");
    }

    @Bean
    @ConditionalOnMissingBean(RedisSerializer.class)
    public RedisSerializer<Object> redisSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 将类型序列化到属性json字符串中
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return new GenericJackson2JsonRedisSerializer(mapper);
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
    @ConditionalOnBean(name = "redisTemplate")
    public RedisService redisService() {
        return new RedisService();
    }

    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    private String limitScriptText() {
        return "local key = KEYS[1]\n" + "local count = tonumber(ARGV[1])\n" + "local time = tonumber(ARGV[2])\n"
                + "local current = redis.call('get', key);\n" + "if current and tonumber(current) > count then\n"
                + "    return tonumber(current);\n" + "end\n" + "current = redis.call('incr', key)\n"
                + "if tonumber(current) == 1 then\n" + "    redis.call('expire', key, time)\n" + "end\n"
                + "return tonumber(current);";
    }
}

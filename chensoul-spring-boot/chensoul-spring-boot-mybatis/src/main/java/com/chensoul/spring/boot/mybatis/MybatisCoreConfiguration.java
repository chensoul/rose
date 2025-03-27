package com.chensoul.spring.boot.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.parser.JsqlParserGlobal;
import com.baomidou.mybatisplus.extension.parser.cache.JdkSerialCaffeineJsqlParseCache;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.chensoul.core.util.NetUtils;
import com.chensoul.mybatis.extension.interceptor.DefaultMetaObjectHandler;
import com.chensoul.mybatis.extension.interceptor.DefaultPaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Slf4j
// 先于 MyBatis Plus 自动配置，避免 @MapperScan 可能扫描不到 Mapper 打印 warn 日志
@AutoConfiguration(before = MybatisPlusAutoConfiguration.class)
public class MybatisCoreConfiguration {
	static {
		// 动态 SQL 智能优化支持本地缓存加速解析，更完善的租户复杂 XML 动态 SQL 支持，静态注入缓存
		JsqlParserGlobal.setJsqlParseCache(new JdkSerialCaffeineJsqlParseCache(
			(cache) -> cache.maximumSize(1024)
				.expireAfterWrite(5, TimeUnit.SECONDS))
		);
	}

	@Bean
	@ConditionalOnMissingBean
	public IdentifierGenerator idGenerator() {
		return new DefaultIdentifierGenerator(NetUtils.getLocalhost());
	}

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new DefaultPaginationInnerInterceptor());
		return mybatisPlusInterceptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public ISqlInjector sqlInjector() {
		return new DefaultSqlInjector();
	}

	@Bean
	@ConditionalOnMissingBean
	public MetaObjectHandler metaObjectHandler() {
		return new DefaultMetaObjectHandler();
	}
}

package com.chensoul.spring.boot.mybatis;

import com.chensoul.mybatis.encrypt.DefaultEncryptor;
import com.chensoul.mybatis.encrypt.IEncryptor;
import com.chensoul.mybatis.encrypt.IFieldBinder;
import com.chensoul.mybatis.encrypt.interceptor.FieldDecryptInterceptor;
import com.chensoul.mybatis.encrypt.interceptor.FieldEncryptInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@Configuration
@ConditionalOnProperty(prefix = "mybatis-plus.encryptor", name = "password")
public class MybatisEncryptorConfiguration {

	@Value("${mybatis-plus.encryptor.password:-123456654321}")
	private String password;

	@Bean
	@ConditionalOnMissingBean
	public FieldEncryptInterceptor fieldEncryptInterceptor(IEncryptor encryptor) {
		return new FieldEncryptInterceptor(encryptor, password);
	}

	@Bean
	@ConditionalOnMissingBean
	public FieldDecryptInterceptor fieldDecryptInterceptor(@Autowired(required = false) IEncryptor encryptor,
														   @Autowired(required = false) IFieldBinder fieldBinder) {
		return new FieldDecryptInterceptor(encryptor, fieldBinder, password);
	}

	@Bean
	@ConditionalOnMissingBean
	public IEncryptor encryptor() {
		return new DefaultEncryptor();
	}

}

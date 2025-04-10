package com.chensoul.spring.boot.oss.config;

import com.chensoul.spring.boot.oss.props.OssProperties;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AllArgsConstructor
@AutoConfigureAfter(OssConfiguration.class)
@ConditionalOnClass({MinioClient.class})
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "tencent")
public class TencentOssConfiguration {

	private final OssProperties ossProperties;

	@Bean
	public COSClient cosClient() {
		COSCredentials credentials = new BasicCOSCredentials(ossProperties.getAccessKey(),
			ossProperties.getSecretKey());
		// 初始化客户端配置
		ClientConfig clientConfig = new ClientConfig(new Region((String) ossProperties.getArgs().get("region")));
		return new COSClient(credentials, clientConfig);
	}

}

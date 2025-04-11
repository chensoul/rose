package com.chensoul.spring.boot.oss.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * Minio参数配置类
 *
 * @author Chill
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

	/**
	 * 是否启用
	 */
	private Boolean enabled;

	/**
	 * 对象存储名称
	 */
	private String name;

	/**
	 * 是否开启租户模式
	 */
	private Boolean tenantMode;

	/**
	 * 对象存储服务的URL
	 */
	private String endpoint;

	/**
	 * 转换外网地址的URL
	 */
	private String transformEndpoint;

	/**
	 * Access key就像用户ID，可以唯一标识你的账户
	 */
	private String accessKey;

	/**
	 * Secret key是你账户的密码
	 */
	private String secretKey;

	/**
	 * 默认的存储桶名称
	 */
	private String bucketName = "rose";

	/**
	 * 自定义属性
	 */
	private LinkedCaseInsensitiveMap<Object> args;
}

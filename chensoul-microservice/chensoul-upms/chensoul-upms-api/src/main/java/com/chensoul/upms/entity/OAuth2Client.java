package com.chensoul.upms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chensoul.mybatis.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_oauth2_client")
public class OAuth2Client extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 客户端id
	 */
	private String clientId;
	/**
	 * 客户端密钥
	 */
	private String clientSecret;
	/**
	 * 资源集合
	 */
	private String resourceIds;
	/**
	 * 授权范围
	 */
	private String scope;
	/**
	 * 授权类型
	 */
	private String authorizedGrantTypes;
	/**
	 * 回调地址
	 */
	private String webServerRedirectUri;
	/**
	 * 权限
	 */
	private String authorities;
	/**
	 * 令牌过期秒数
	 */
	private Integer accessTokenValidity;
	/**
	 * 刷新令牌过期秒数
	 */
	private Integer refreshTokenValidity;
	/**
	 * 附加说明
	 */
	private String additionalInformation;
	/**
	 * 自动授权
	 */
	private String autoapprove;

	private Integer status;

}

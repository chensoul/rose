package com.chensoul.spring.boot.xxljob;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * XXL-Job 配置类
 */
@ConfigurationProperties("xxl.xxljob")
@Validated
@Data
public class XxlJobProperties {

	/**
	 * 访问令牌
	 */
	private String accessToken;

	/**
	 * 控制器配置
	 */
	private AdminProperties admin = new AdminProperties();

	/**
	 * 执行器配置
	 */
	private ExecutorProperties executor = new ExecutorProperties();

	private ClientProperties client = new ClientProperties();

	/**
	 * XXL-Job 调度器配置类
	 */
	@Data
	public static class AdminProperties {

		/**
		 * 调度器地址
		 */
		@NotEmpty(message = "调度器地址不能为空")
		private String addresses;

	}

	/**
	 * XXL-Job 执行器配置类
	 */
	@Data
	@Valid
	public static class ExecutorProperties {

		/**
		 * 默认端口
		 * <p>
		 * 这里使用 -1 表示随机
		 */
		private static final Integer PORT_DEFAULT = -1;

		/**
		 * 默认日志保留天数
		 * <p>
		 * 如果想永久保留，则设置为 -1
		 */
		private static final Integer LOG_RETENTION_DAYS_DEFAULT = 30;

		private Boolean enabled = true;

		/**
		 * 应用名
		 */
		private String appName;

		/**
		 * 执行器的 IP
		 */
		private String ip;

		/**
		 * 执行器的 Port
		 */
		private Integer port = PORT_DEFAULT;

		/**
		 * 日志地址
		 */
		private String logPath;

		/**
		 * 日志保留天数
		 */
		private Integer logRetentionDays = LOG_RETENTION_DAYS_DEFAULT;

	}

	@Valid
	@Data
	public static class ClientProperties {

		private Boolean enabled = false;

		@NotBlank(message = "XxlJob用户名不能为空")
		private String username;

		@NotBlank(message = "XxlJob密码不能为空")
		private String password;

		private String author = "admin";

		private String alarmEmail;

		private String scheduleType = "CRON";

		private String glueType = "BEAN";

		private String executorRouteStrategy = "ROUND";

		private String misfireStrategy = "DO_NOTHING";

		private String executorBlockStrategy = "SERIAL_EXECUTION";

		private int executorTimeout = 0;

		private int executorFailRetryCount = 0;

	}

}

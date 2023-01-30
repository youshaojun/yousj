package top.yousj.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.yousj.web.constant.PropertyConstant;

/**
 * @author yousj
 * @since 2023-01-30
 */
@Data
@ConfigurationProperties(prefix = PropertyConstant.WEB)
public class WebProperties {

	/**
	 * okhttp配置
	 */
	private Okhttp okhttp = new Okhttp();

	@Data
	public static class Okhttp {

		/**
		 * 连接超时时间
		 */
		private Long connectTimeout = 10L;

		/**
		 * 读超时时间
		 */
		private Long readTimeout = 300L;

		/**
		 * 写超时时间
		 */
		private Long writeTimeout = 300L;

		/**
		 * 代理
		 */
		private Proxy proxy;

		@Data
		public static class Proxy {

			/**
			 * 账号
			 */
			private String username;

			/**
			 * 密码
			 */
			private String password;

			/**
			 * 地址
			 */
			private String addr;

			/**
			 * 端口
			 */
			private Integer port;

		}

	}

}

package top.yousj.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "top.yousj")
public class TopYousjProperties {

	/**
	 * 请求响应加解密配置
	 */
	private Crypto crypto;

	/**
	 * web日志切面配置
	 */
	private Log log;

	/**
	 * redis配置
	 */
	private Redis redis;

	/**
	 * security配置
	 */
	private Security security;

	@Data
	public static class Crypto {

		private Decrypt decrypt;

		private Encrypt encrypt;

		@Data
		public static class Decrypt {

			/**
			 * 请求参数解密
			 */
			private boolean enable = true;

		}

		@Data
		public static class Encrypt {

			/**
			 * 响应结果加密
			 */
			private boolean enable = true;

		}

	}

	@Data
	public static class Log {

		/**
		 * web日志切面
		 */
		private Aop aop;

		@Data
		public static class Aop {

			/**
			 * 排序
			 */
			private Integer order = -1;

			/**
			 * 切面el表达式
			 */
			private String pointcut;

		}

	}

	@Data
	public static class Redis {

		private boolean enable = true;

		/**
		 * 扫描开启支持自定义过期时间的spring cache的包
		 */
		private List<String> scanPackages;

	}

	@Data
	public static class Security {

		/**
		 * jwt配置
		 */
		private Jwt jwt;

		@Data
		public static class Jwt {

			/**
			 * 过期时间
			 */
			private Long expire = 86400000L;

			/**
			 * jwt签名key
			 */
			private String signKey = "jwt_secret:";

		}

	}

}

package top.yousj.rest.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.rest.constant.PropertyConstant;

import java.util.List;

/**
 * @author yousj
 * @since 2023-01-30
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.OKHTTP)
public class OkhttpProperties {

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
	 * 静态IP代理
	 */
	private Proxy proxy;

	/**
	 * 动态IP池代理
	 */
	private Pool pool;

	@Data
	@Accessors(chain = true)
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

	@Data
	public static class Pool {
		private List<String> urls;
	}

}

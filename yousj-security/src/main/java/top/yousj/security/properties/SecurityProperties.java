package top.yousj.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.security.constant.PropertyConstant;

/**
 * @author yousj
 * @since 2023-01-10
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.SECURITY)
public class SecurityProperties {

	/**
	 * jwt配置
	 */
	private Jwt jwt = new Jwt();

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
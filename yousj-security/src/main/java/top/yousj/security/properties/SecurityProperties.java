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
	 * 开启{@link org.springframework.http.HttpStatus}
	 */
	private boolean httpStatus = true;

	/**
	 * 开启集中式权限控制
	 */
	private boolean multiple;

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

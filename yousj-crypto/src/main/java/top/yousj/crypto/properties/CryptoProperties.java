package top.yousj.crypto.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.crypto.constant.PropertyConstant;

/**
 * 请求响应加解密配置
 *
 * @author yousj
 * @since 2023-01-10
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.CRYPTO)
public class CryptoProperties {

	private Decrypt decrypt = new Decrypt();

	private Encrypt encrypt = new Encrypt();

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

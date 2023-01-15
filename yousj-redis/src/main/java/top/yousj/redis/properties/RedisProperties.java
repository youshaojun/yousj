package top.yousj.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.redis.constant.PropertyConstant;

import java.util.Collections;
import java.util.List;

/**
 * @author yousj
 * @since 2023-01-10
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.REDIS)
public class RedisProperties {

	private SpringCache springCache = new SpringCache();

	@Data
	public static class SpringCache {

		private boolean enable = true;

		/**
		 * 扫描开启支持自定义过期时间的spring cache的包
		 */
		private List<String> scanPackages = Collections.singletonList("top.yousj");

	}

}

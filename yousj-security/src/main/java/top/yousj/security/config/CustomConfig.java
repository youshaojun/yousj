package top.yousj.security.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import top.yousj.core.constant.UaaConstant;

import java.util.Map;
import java.util.Set;

/**
 * @author yousj
 * @since 2023-01-06
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CustomConfig {

	/**
	 * 直接放行的path
	 */
	public static final Set<String> IGNORE_URLS = Sets.newConcurrentHashSet();

	static {
		IGNORE_URLS.add("/reload/updateClass");
		IGNORE_URLS.add("/reload/updateMapperXml");
		IGNORE_URLS.add("/favicon.ico");
		IGNORE_URLS.add("/doc.html");
		IGNORE_URLS.add("/swagger-ui.html");
		IGNORE_URLS.add("/css/**");
		IGNORE_URLS.add("/js/**");
		IGNORE_URLS.add("/docs/**");
		IGNORE_URLS.add("/webjars/**");
		IGNORE_URLS.add("/v2/**");
		IGNORE_URLS.add("/v3/**");
		IGNORE_URLS.add("/swagger-resources/**");
		IGNORE_URLS.add("/swagger-ui/**");

		IGNORE_URLS.add("/actuator/**");
		IGNORE_URLS.add("/druid/**");

		IGNORE_URLS.add("/login");
	}

	/**
	 * 单项目path过滤
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Alone {

		/**
		 * 全部的path, 不存在返回404
		 */
		public static final Set<String> ALL_URLS = Sets.newConcurrentHashSet();

		/**
		 * 登录后可访问path
		 */
		public static final Set<String> AUTH_PERMIT_URLS = Sets.newConcurrentHashSet();

	}

	/**
	 * 集中式path过滤
	 * key -> {@link UaaConstant#APP_NAME}
	 * vale ->  path {@link Alone} 需要包含工程名称{@link org.springframework.boot.autoconfigure.web.ServerProperties.Servlet#contextPath}
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Multiple {

		public static final Map<String, Set<String>> ALL_URLS = Maps.newConcurrentMap();

		public static final Map<String, Set<String>> AUTH_PERMIT_URLS = Maps.newConcurrentMap();

		public static final Map<String, Set<String>> SELF_IGNORE_URLS = Maps.newConcurrentMap();

	}

}

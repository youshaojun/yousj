package top.yousj.security.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yousj
 * @since 2023-01-06
 */
@EnableScheduling
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CustomConfig {

	public static Map<String, Set<String>> ALL_URLS = Maps.newConcurrentMap();

	public static Map<String, Set<String>> AUTH_PERMIT_URLS = Maps.newConcurrentMap();

	public static Map<String, Set<String>> IGNORE_URLS = Maps.newConcurrentMap();

	public static Set<String> COMMON_IGNORE_URLS = Sets.newConcurrentHashSet();

	static {
		COMMON_IGNORE_URLS.add("/reload/updateClass");
		COMMON_IGNORE_URLS.add("/reload/updateMapperXml");
		COMMON_IGNORE_URLS.add("/actuator/**");
		COMMON_IGNORE_URLS.add("/favicon.ico");
		COMMON_IGNORE_URLS.add("/doc.html");
		COMMON_IGNORE_URLS.add("/swagger-ui.html");
		COMMON_IGNORE_URLS.add("/css/**");
		COMMON_IGNORE_URLS.add("/js/**");
		COMMON_IGNORE_URLS.add("/docs/**");
		COMMON_IGNORE_URLS.add("/webjars/**");
		COMMON_IGNORE_URLS.add("/v2/**");
		COMMON_IGNORE_URLS.add("/v3/**");
		COMMON_IGNORE_URLS.add("/swagger-resources/**");
		COMMON_IGNORE_URLS.add("/swagger-ui/**");
		COMMON_IGNORE_URLS.add("/druid/**");
	}

	private final CustomConfigReloadHandler customConfigReloadHandler;

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
	public void reloadConfig() {
		customConfigReloadHandler.reloadAllUrls();
		customConfigReloadHandler.reloadAuthPermitUrls();
		customConfigReloadHandler.reloadIgnoreUrls();
		customConfigReloadHandler.reloadCommonIgnoreUrls();
	}

}

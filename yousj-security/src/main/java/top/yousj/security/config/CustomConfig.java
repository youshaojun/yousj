package top.yousj.security.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
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
public class CustomConfig {

	private final CustomConfigReloadHandler customConfigReloadHandler;

	public static Map<String, Set<String>> AUTH_PERMIT_URLS = Maps.newConcurrentMap();

	public static Map<String, Set<String>> IGNORE_URLS = Maps.newConcurrentMap();

	public static Set<String> COMMON_IGNORE_URLS = Sets.newHashSet();

	static {
		COMMON_IGNORE_URLS.add("/reload/updateClass");
		COMMON_IGNORE_URLS.add("/reload/updateMapperXml");
	}

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
	public void reloadConfig() {
		customConfigReloadHandler.reloadAuthPermitUrls();
		customConfigReloadHandler.reloadIgnoreUrls();
		customConfigReloadHandler.reloadCommonIgnoreUrls();
	}

}

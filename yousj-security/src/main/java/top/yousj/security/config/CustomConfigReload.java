package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

import static top.yousj.security.config.CustomConfig.COMMON_IGNORE_URLS;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class CustomConfigReload {

	private final CustomConfigReloadHandler customConfigReloadHandler;

	@Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
	public void reloadConfig() {
		customConfigReloadHandler.reloadAllUrls();
		customConfigReloadHandler.reloadAuthPermitUrls();
		customConfigReloadHandler.reloadIgnoreUrls();
		customConfigReloadHandler.reloadCommonIgnoreUrls();
	}

	public interface CustomConfigReloadHandler {

		/**
		 * {@link CustomConfig#COMMON_IGNORE_URLS}
		 */
		default void reloadCommonIgnoreUrls() {
		}

		/**
		 * {@link CustomConfig.Alone#ALL_URLS}
		 * {@link CustomConfig.Multiple#ALL_URLS}
		 */
		default void reloadAllUrls() {
		}

		/**
		 * {@link CustomConfig.Alone#AUTH_PERMIT_URLS}
		 * {@link CustomConfig.Multiple#AUTH_PERMIT_URLS}
		 */
		default void reloadAuthPermitUrls() {
		}

		/**
		 * {@link CustomConfig.Alone#IGNORE_URLS}
		 * {@link CustomConfig.Multiple#IGNORE_URLS}
		 */
		default void reloadIgnoreUrls() {
		}

	}

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
}

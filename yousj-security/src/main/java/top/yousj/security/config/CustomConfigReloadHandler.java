package top.yousj.security.config;

/**
 * @author yousj
 * @since 2023-01-06
 */
public interface CustomConfigReloadHandler {

	/**
	 * {@link CustomConfig#AUTH_PERMIT_URLS}
	 */
	default void reloadAuthPermitUrls() {
	}

	/**
	 * {@link CustomConfig#IGNORE_URLS}
	 */
	default void reloadIgnoreUrls() {
	}

	/**
	 * {@link CustomConfig#COMMON_IGNORE_URLS}
	 */
	default void reloadCommonIgnoreUrls() {
	}

}

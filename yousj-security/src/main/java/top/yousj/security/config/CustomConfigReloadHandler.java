package top.yousj.security.config;

/**
 * @author yousj
 * @since 2023-01-06
 */
public interface CustomConfigReloadHandler {

	default void reloadAuthPermitUrls() {
	}

	default void reloadIgnoreUrls() {
	}

	default void reloadCommonIgnoreUrls() {
	}

}

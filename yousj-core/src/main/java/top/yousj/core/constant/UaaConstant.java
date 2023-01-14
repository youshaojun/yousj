package top.yousj.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author yousj
 * @since 2023-01-05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UaaConstant {

	public static final String TOKEN_HEADER = "token";

	public static final String AUTHORIZATION = "Authorization";

	public static final String APP_PREFIX = "Top-Yousj-App-";

	public static final String APP_NAME = APP_PREFIX + "Name";

	public static final String APP_CHANNEL = APP_PREFIX + "Channel";

	/**
	 * @see <a href="https://apisix.apache.org/zh/docs/apisix/next/plugins/forward-auth/">apisix认证插件响应头</a>
	 */
	public static final String FORWARD_AUTH_HEADER_USER_ID = "X-User-ID";
	public static final String FORWARD_AUTH_HEADER_SCHEME = "X-Forwarded-Proto";
	public static final String FORWARD_AUTH_HEADER_HTTP_METHOD = "X-Forwarded-Method";
	public static final String FORWARD_AUTH_HEADER_HOST = "X-Forwarded-Host";
	public static final String FORWARD_AUTH_HEADER_URI = "X-Forwarded-Uri";
	public static final String FORWARD_AUTH_HEADER_SOURCE_IP = "X-Forwarded-For";

}

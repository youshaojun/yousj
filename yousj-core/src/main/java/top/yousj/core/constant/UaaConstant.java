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

	public static final String APP_PREFIX = "top-yousj-app-";

	public static final String APP_UID = APP_PREFIX + "uid";

	public static final String APP_NAME = APP_PREFIX + "name";

	public static final String APP_CHANNEL = APP_PREFIX + "channel";

}

package top.yousj.core.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author yousj
 * @since 2023-01-05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UaaConstant {

	public static final String TOKEN_HEADER = "Token";

	public static final String AUTHORIZATION = "Authorization";

	public static final String APP_PREFIX = "Top-Yousj-App-";

	public static final String APP_UID = APP_PREFIX + "User-Id";

	public static final String APP_NAME = APP_PREFIX + "Name";

	public static final String APP_CHANNEL = APP_PREFIX + "Channel";

}

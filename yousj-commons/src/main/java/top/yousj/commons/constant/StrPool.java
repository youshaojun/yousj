package top.yousj.commons.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author yousj
 * @since 2023-01-06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StrPool {

	public static final String EMPTY = "";

	public static final String COMMA = ",";

	public static final String CHARSET_NAME = "UTF-8";

	public static final String BEARER = "Bearer ";

	public static final String TMP = "/tmp";

	public static final String SLASH = "/";

	public static final String BACKSLASH = "\\";

	public static final String UNKNOWN = "unknown";

	public static final String LOCALHOST = "127.0.0.1";

    public static final String[] IP_HEADER_NAMES = new String[]{"x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};


}

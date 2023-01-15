package top.yousj.core.constant;

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

	public static final String CRYPTO_AES = "AES";

	public static final String CRYPTO_RSA = "RSA";

	public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	public static final String BEARER = "Bearer ";

	public static final String TMP = "/tmp";

}

package top.yousj.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import top.yousj.core.constant.StrPool;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES对称加密
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESUtil {

	@SneakyThrows
	private static Cipher getCipher(byte[] key, int model) {
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, StrPool.CRYPTO_AES);
		Cipher cipher = Cipher.getInstance(StrPool.AES_ALGORITHM);
		cipher.init(model, secretKeySpec);
		return cipher;
	}

	@SneakyThrows
	public static String encrypt(String body, String key) {
		return Base64.getEncoder().encodeToString(getCipher(key.getBytes(), Cipher.ENCRYPT_MODE).doFinal(body.getBytes()));
	}

	@SneakyThrows
	public static String decrypt(String body, String key) {
		return new String(getCipher(key.getBytes(), Cipher.DECRYPT_MODE).doFinal(Base64.getDecoder().decode(body.getBytes())), StrPool.CHARSET_NAME);
	}

}

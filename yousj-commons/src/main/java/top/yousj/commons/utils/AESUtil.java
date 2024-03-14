package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import top.yousj.commons.constant.StrPool;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES对称加密
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AESUtil {

    private static final String CRYPTO_AES = "AES";
    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    @SneakyThrows
    public static SecretKey generateAesKey() {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CRYPTO_AES);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }

    @SneakyThrows
    private static Cipher getCipher(byte[] key, int model) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, CRYPTO_AES);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(model, secretKeySpec);
        return cipher;
    }

    @SneakyThrows
    public static String encrypt(String body, String key) {
        return Base64.getEncoder().encodeToString(getCipher(base64decode(key), Cipher.ENCRYPT_MODE).doFinal(body.getBytes()));
    }

    @SneakyThrows
    public static String decrypt(String body, String key) {
        return new String(getCipher(base64decode(key), Cipher.DECRYPT_MODE).doFinal(base64decode(body)), StrPool.CHARSET_NAME);
    }

    private static byte[] base64decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static void main(String[] args) {

        SecretKey secretKey = generateAesKey();
        String aesKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("aes key:  " + aesKey);

        String encrypt = encrypt("hi", aesKey);
        System.out.println(encrypt);
        System.out.println(decrypt(encrypt, aesKey));
    }

}

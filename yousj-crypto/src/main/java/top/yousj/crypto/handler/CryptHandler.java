package top.yousj.crypto.handler;

import top.yousj.crypto.properties.KeyProperties;

/**
 * @author yousj
 * @since 2023-01-06
 */
public interface CryptHandler {

	String encrypt(String body, KeyProperties keyProperties);

	String decrypt(String body, KeyProperties keyProperties);

}

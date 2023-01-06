package top.yousj.crpyto.handler;

import top.yousj.crpyto.config.KeyProperties;

/**
 * @author yousj
 * @since 2023-01-06
 */
public interface CryptHandler {

	String encrypt(String body, KeyProperties keyProperties);

	String decrypt(String body, KeyProperties keyProperties);

}

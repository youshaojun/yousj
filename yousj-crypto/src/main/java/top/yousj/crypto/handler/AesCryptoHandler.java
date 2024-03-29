package top.yousj.crypto.handler;

import top.yousj.commons.utils.AESUtil;
import top.yousj.crypto.properties.KeyProperties;

/**
 * @author yousj
 * @since 2023-01-06
 */
public class AesCryptoHandler implements CryptoHandler {

	@Override
	public String encrypt(String body, KeyProperties keyProperties) {
		return AESUtil.encrypt(body, keyProperties.getPublicKey());
	}

	@Override
	public String decrypt(String body, KeyProperties keyProperties) {
		return AESUtil.decrypt(body, keyProperties.getPublicKey());
	}
}

package top.yousj.crpyto.handler;

import top.yousj.core.utils.AESUtil;
import top.yousj.crpyto.config.KeyProperties;

/**
 * @author yousj
 * @since 2023-01-06
 */
public class AesCryptHandler implements CryptHandler {

	@Override
	public String encrypt(String body, KeyProperties keyProperties) {
		return AESUtil.encrypt(body, keyProperties.getPublicKey());
	}

	@Override
	public String decrypt(String body, KeyProperties keyProperties) {
		return AESUtil.decrypt(body, keyProperties.getPublicKey());
	}
}

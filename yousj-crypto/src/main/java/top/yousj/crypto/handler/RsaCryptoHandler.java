package top.yousj.crypto.handler;

import top.yousj.commons.utils.RSAUtil;
import top.yousj.crypto.properties.KeyProperties;

/**
 * @author yousj
 * @since 2023-01-06
 */
public class RsaCryptoHandler implements CryptoHandler {

	@Override
	public String encrypt(String body, KeyProperties keyProperties) {
		return RSAUtil.encrypt(body, keyProperties.getPublicKey());
	}

	@Override
	public String decrypt(String body, KeyProperties keyProperties) {
		return RSAUtil.decrypt(body, keyProperties.getPrivateKey());
	}
}

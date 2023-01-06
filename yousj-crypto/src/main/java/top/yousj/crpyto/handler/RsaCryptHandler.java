package top.yousj.crpyto.handler;

import top.yousj.crpyto.config.KeyProperties;
import top.yousj.crpyto.utils.RSAUtil;

/**
 * @author yousj
 * @since 2023-01-06
 */
public class RsaCryptHandler implements CryptHandler {

	@Override
	public String encrypt(String body, KeyProperties keyProperties) {
		return RSAUtil.encrypt(body, keyProperties.getPublicKey());
	}

	@Override
	public String decrypt(String body, KeyProperties keyProperties) {
		return RSAUtil.decrypt(body, keyProperties.getPrivateKey());
	}
}

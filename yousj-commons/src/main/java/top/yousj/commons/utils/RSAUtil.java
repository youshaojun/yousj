package top.yousj.commons.utils;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import top.yousj.commons.constant.StrPool;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA非对称加密
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RSAUtil {

	private static final String CRYPTO_RSA = "RSA";

	public static void main(String[] args) {
		generateRsaKey(2048);
	}

	public static void generateRsaKey(int keySize) {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(CRYPTO_RSA);
			// 初始化密钥对生成器，密钥大小为1024 2048位
			keyPairGen.initialize(keySize, new SecureRandom());
			// 生成一个密钥对，保存在keyPair中
			KeyPair keyPair = keyPairGen.generateKeyPair();
			System.out.println("公钥为：" + new String(Base64.encodeBase64(keyPair.getPublic().getEncoded())));
			System.out.println("私钥为：" + new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded())));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	@SneakyThrows
	public static String encrypt(String body, String publicKey) {
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(CRYPTO_RSA).generatePublic(new X509EncodedKeySpec(decoded));
		Cipher cipher = Cipher.getInstance(CRYPTO_RSA);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return Base64.encodeBase64String(cipher.doFinal(body.getBytes(StrPool.CHARSET_NAME)));
	}

	public static String decrypt(String str, String privateKey) {
		return Try.of(() -> {
			byte[] inputByte = Base64.decodeBase64(str.getBytes(StrPool.CHARSET_NAME));
			byte[] decoded = Base64.decodeBase64(privateKey);
			RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(CRYPTO_RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
			Cipher cipher = Cipher.getInstance(CRYPTO_RSA);
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			return new String(cipher.doFinal(inputByte));
		}).get();
	}

}

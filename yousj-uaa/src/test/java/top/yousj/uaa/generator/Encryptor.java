package top.yousj.uaa.generator;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class Encryptor {

    /**
     * 生成加密结果
     */
    public static String encrypt(String salt, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		encryptor.setConfig(cryptor(salt));
        return encryptor.encrypt(value);
    }

    /**
     * 解密
     */
    public static String decrypt(String salt, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptor(salt));
        return encryptor.decrypt(value);
    }

    public static SimpleStringPBEConfig cryptor(String salt) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(salt);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args) {
        String encStr = encrypt("test", "root");
        String decStr = decrypt("test", encStr);
        System.out.println(encStr);
        System.out.println(decStr);
    }

}

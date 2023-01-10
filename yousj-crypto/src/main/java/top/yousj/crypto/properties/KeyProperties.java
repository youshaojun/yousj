package top.yousj.crypto.properties;

import lombok.Data;

/**
 * @author yousj
 * @since 2023-01-06
 */
@Data
public class KeyProperties {

	private String publicKey;

	private String privateKey;

	private String salt;

}

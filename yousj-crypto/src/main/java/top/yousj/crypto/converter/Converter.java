package top.yousj.crypto.converter;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import top.yousj.commons.constant.StrPool;
import top.yousj.commons.entity.R;
import top.yousj.commons.utils.JsonUtil;
import top.yousj.commons.utils.SpringUtil;
import top.yousj.crypto.handler.CryptHandler;
import top.yousj.crypto.properties.KeyProperties;
import top.yousj.crypto.properties.KeyPropertiesResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author yousj
 * @since 2023-02-10
 */
@RequiredArgsConstructor
public class Converter {

	private final KeyPropertiesResolver keyPropertiesResolver;
	private final HttpServletRequest request;

	@SneakyThrows
	@SuppressWarnings("unchecked")
	public String convert(Class<? extends CryptHandler> clazz, boolean encrypt, boolean onlyData, Object body) {
		if (Objects.isNull(body)) {
			return null;
		}
		CryptHandler handler = SpringUtil.getBean(clazz);
		String bodyStr = encrypt ? JsonUtil.toJson(body) : new String((byte[]) body, StrPool.CHARSET_NAME);
		KeyProperties keyProperties = keyPropertiesResolver.getKeyProperties(request);
		if (!encrypt) {
			return handler.decrypt(bodyStr, keyProperties);
		}
		if (!onlyData) {
			return handler.encrypt(bodyStr, keyProperties);
		}

		R r = JsonUtil.fromJson(bodyStr, R.class);
		Object data = r.getData();
		if (Objects.isNull(data)) {
			return bodyStr;
		}
		bodyStr = JsonUtil.toJson(data);
		r.setData(handler.encrypt(bodyStr, keyProperties));
		return JsonUtil.toJson(r);
	}

}

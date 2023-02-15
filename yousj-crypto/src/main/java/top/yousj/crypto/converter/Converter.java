package top.yousj.crypto.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import top.yousj.commons.constant.StrPool;
import top.yousj.commons.entity.R;
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

	private final KeyPropertiesResolver keyPropertiesHolder;
	private final HttpServletRequest request;
	private final ObjectMapper objectMapper;

	@SneakyThrows
	@SuppressWarnings("unchecked")
	public String convert(Class<? extends CryptHandler> clazz, boolean encrypt, boolean onlyData, Object body) {
		if (Objects.isNull(body)) {
			return null;
		}
		CryptHandler handler = SpringUtil.getBean(clazz);
		String bodyStr = encrypt ? objectMapper.writeValueAsString(body) : new String((byte[]) body, StrPool.CHARSET_NAME);
		KeyProperties keyProperties = keyPropertiesHolder.getKeyProperties(request);
		if (!onlyData) {
			return encrypt ? handler.encrypt(bodyStr, keyProperties) : handler.decrypt(bodyStr, keyProperties);
		}
		R r = objectMapper.readValue(bodyStr, R.class);
		Object data = r.getData();
		if (Objects.isNull(data)) {
			return bodyStr;
		}
		bodyStr = data.toString();
		r.setData(encrypt ? handler.encrypt(bodyStr, keyProperties) : handler.decrypt(bodyStr, keyProperties));
		return objectMapper.writeValueAsString(r);
	}

}

package top.yousj.crypto.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import top.yousj.core.constant.StrPool;
import top.yousj.core.entity.R;
import top.yousj.core.utils.SpringUtil;
import top.yousj.crypto.config.KeyProperties;
import top.yousj.crypto.config.KeyPropertiesHolder;
import top.yousj.crypto.handler.CryptHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AdviceCryptUtil {

	private final KeyPropertiesHolder keyPropertiesHolder;
	private final HttpServletRequest request;
	private final ObjectMapper objectMapper;

	@SneakyThrows
	@SuppressWarnings("unchecked")
	public String handle(Class<? extends CryptHandler> handler, boolean encrypt, boolean onlyData, Object body) {
		if (Objects.isNull(body)) {
			return null;
		}
		CryptHandler cryptHandlerBean = SpringUtil.getBean(handler);
		String bodyStr = encrypt ? objectMapper.writeValueAsString(body) : new String((byte[]) body, StrPool.CHARSET_NAME);
		KeyProperties keyProperties = keyPropertiesHolder.getKeyProperties(request);
		if (!onlyData) {
			return encrypt ? cryptHandlerBean.encrypt(bodyStr, keyProperties) : cryptHandlerBean.decrypt(bodyStr, keyProperties);
		}
		R r = objectMapper.readValue(bodyStr, R.class);
		Object data = r.getData();
		if (Objects.isNull(data)) {
			return bodyStr;
		}
		bodyStr = String.valueOf(data);
		r.setData(encrypt ? cryptHandlerBean.encrypt(bodyStr, keyProperties) : cryptHandlerBean.decrypt(bodyStr, keyProperties));
		return objectMapper.writeValueAsString(r);
	}

}

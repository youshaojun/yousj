package top.yousj.crypto.properties;

import top.yousj.core.constant.UaaConstant;
import top.yousj.core.utils.ParamAssertUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-01-06
 */
public interface KeyPropertiesHolder {

	KeyProperties getKeyProperties(String channel);

	default KeyProperties getKeyProperties(HttpServletRequest request) {
		String channel = request.getHeader(UaaConstant.APP_CHANNEL);
		ParamAssertUtil.notNull(channel, "channel can't be null.");
		return getKeyProperties(channel);
	}

}

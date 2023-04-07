package top.yousj.crypto.properties;

import top.yousj.commons.constant.UaaConstant;
import top.yousj.commons.utils.ParamAssertUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-01-06
 */
public interface KeyPropertiesResolver {

	KeyProperties resolve(String channel);

	default KeyProperties resolve(HttpServletRequest request) {
		String channel = request.getHeader(UaaConstant.APP_CHANNEL);
		ParamAssertUtil.notNull(channel, "channel can't be null.");
		return resolve(channel);
	}

}

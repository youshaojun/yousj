package top.yousj.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.exception.BizException;

import javax.servlet.http.HttpServletRequest;

@Component
public class UaaUtil {

	public static Integer getUserId(HttpServletRequest request) {
		return Integer.valueOf(request.getHeader(UaaConstant.FORWARD_AUTH_HEADER_USER_ID));
	}

	public static String getAppName(HttpServletRequest request, boolean isUaa) {
		String appName = request.getHeader(UaaConstant.APP_NAME);
		if (isUaa && StringUtils.isBlank(appName)) {
			throw new BizException("app name can not be blank.");
		}
		return appName;
	}

}

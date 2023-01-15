package top.yousj.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.exception.BizException;

import javax.servlet.http.HttpServletRequest;

@Component
public class UaaUtil {

	private static HttpServletRequest request;

	@Autowired
	public UaaUtil(HttpServletRequest request){
		UaaUtil.request = request;
	}

	public static Integer getUserId() {
		return Integer.valueOf(request.getHeader(UaaConstant.FORWARD_AUTH_HEADER_USER_ID));
	}

	public static String getAppName() {
		String appName = request.getHeader(UaaConstant.APP_NAME);
		if(StringUtils.isBlank(appName)){
			throw new BizException("app name can not be blank.");
		}
		return appName;
	}

}

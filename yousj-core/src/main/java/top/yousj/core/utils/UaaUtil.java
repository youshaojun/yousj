package top.yousj.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yousj.core.constant.UaaConstant;

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
		return request.getHeader(UaaConstant.APP_NAME);
	}

}

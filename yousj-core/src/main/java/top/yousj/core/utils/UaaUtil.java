package top.yousj.core.utils;

import top.yousj.core.constant.UaaConstant;

import javax.servlet.http.HttpServletRequest;

public class UaaUtil {

	public static Integer getUserId(HttpServletRequest request) {
		return Integer.valueOf(request.getHeader(UaaConstant.FORWARD_AUTH_HEADER_USER_ID));
	}

}

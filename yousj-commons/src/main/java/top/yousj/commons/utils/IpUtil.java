package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import top.yousj.commons.constant.UaaConstant;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-02-15
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtil {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader(UaaConstant.FORWARD_AUTH_HEADER_SOURCE_IP);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if ("0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "127.0.0.1";
		}
		if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
		return ip;
	}

}

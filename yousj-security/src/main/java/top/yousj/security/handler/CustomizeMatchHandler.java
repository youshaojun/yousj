package top.yousj.security.handler;

import top.yousj.security.properties.SecurityProperties;

import javax.servlet.http.HttpServletRequest;

public interface CustomizeMatchHandler {

	/**
	 * 已登录可访问
	 */
	boolean matchAuthPermitUrls(HttpServletRequest request);

	/**
	 * 未登录可访问
	 */
	boolean matchIgnoreUrls(HttpServletRequest request);

	/**
	 * jwt配置
	 */
	SecurityProperties.Jwt getJwt();

}
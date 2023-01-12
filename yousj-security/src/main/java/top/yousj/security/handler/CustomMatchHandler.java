package top.yousj.security.handler;

import top.yousj.security.properties.SecurityProperties;

import javax.servlet.http.HttpServletRequest;

public interface CustomMatchHandler {

	boolean matchAuthPermitUrls(HttpServletRequest request);

	boolean matchIgnoreUrls(HttpServletRequest request);

	SecurityProperties.Jwt getJwt();

}
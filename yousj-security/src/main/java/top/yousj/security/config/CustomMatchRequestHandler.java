package top.yousj.security.config;

import javax.servlet.http.HttpServletRequest;

public interface CustomMatchRequestHandler {

		boolean matchAuthPermitUrls(HttpServletRequest request);

		boolean matchIgnoreUrls(HttpServletRequest request);

	}
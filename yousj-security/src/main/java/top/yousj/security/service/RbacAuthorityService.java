package top.yousj.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import top.yousj.security.config.CustomConfig;
import top.yousj.security.utils.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-01-09
 */
@Component
@RequiredArgsConstructor
public class RbacAuthorityService {

	private final CustomConfig.CustomMatchRequestHandler customMatchRequestHandler;

	public boolean hasPermission(HttpServletRequest request) {
		if (customMatchRequestHandler.matchAuthPermitUrls(request)) {
			return true;
		}
		return SecurityUtil.getAuthorities().stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}

}

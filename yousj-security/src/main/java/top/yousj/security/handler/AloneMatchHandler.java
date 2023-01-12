package top.yousj.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BizException;
import top.yousj.core.utils.SpringUtil;
import top.yousj.security.config.CustomConfig;
import top.yousj.security.properties.SecurityProperties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-01-11
 */
@ConditionalOnMissingBean(CustomMatchHandler.class)
@RequiredArgsConstructor
public class AloneMatchHandler implements CustomMatchHandler {

	private final SecurityProperties securityProperties;

	@Override
	public boolean matchAuthPermitUrls(HttpServletRequest request) {
		return CustomConfig.Alone.AUTH_PERMIT_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}

	@Override
	public boolean matchIgnoreUrls(HttpServletRequest request) {
		if (CustomConfig.IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomConfig.Alone.ALL_URLS.stream().noneMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			throw new BizException(ResultCode.NOT_FOUND);
		}
		return false;
	}

	@Override
	public SecurityProperties.Jwt getJwt() {
		return securityProperties.getJwt();
	}

	@PostConstruct
	public void initAloneConfig() {
		CustomConfig.Alone.ALL_URLS.addAll(SpringUtil.getMappingUrls());
		CustomConfig.Alone.AUTH_PERMIT_URLS.add("/logout");
	}

}

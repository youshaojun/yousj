package top.yousj.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.yousj.commons.enums.ResultCode;
import top.yousj.commons.exception.BizException;
import top.yousj.commons.utils.SpringUtil;
import top.yousj.security.config.CustomizeConfig;
import top.yousj.security.properties.SecurityProperties;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-01-11
 */
@ConditionalOnMissingBean(CustomizeMatchHandler.class)
@RequiredArgsConstructor
public class AloneMatchHandler implements CustomizeMatchHandler {

	private final SecurityProperties securityProperties;

	@Override
	public boolean matchAuthPermitUrls(HttpServletRequest request) {
		return CustomizeConfig.Alone.AUTH_PERMIT_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}

	@Override
	public boolean matchIgnoreUrls(HttpServletRequest request) {
		if (!securityProperties.isDynamicConfiguration()) {
			return true;
		}
		if (CustomizeConfig.IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomizeConfig.Alone.ALL_URLS.stream().noneMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
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
		CustomizeConfig.Alone.ALL_URLS.addAll(SpringUtil.getMappingUrls());
		CustomizeConfig.Alone.AUTH_PERMIT_URLS.add("/logout");
	}

}

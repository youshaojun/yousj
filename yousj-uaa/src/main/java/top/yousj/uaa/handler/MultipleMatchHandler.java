package top.yousj.uaa.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BizException;
import top.yousj.core.utils.UaaUtil;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.config.CustomConfig;
import top.yousj.security.handler.CustomMatchHandler;
import top.yousj.security.properties.SecurityProperties;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class MultipleMatchHandler implements CustomMatchHandler {

	private final SecurityProperties securityProperties;

	@Override
	public boolean matchAuthPermitUrls(HttpServletRequest request) {
		return CustomConfig.Multiple.AUTH_PERMIT_URLS.get(UaaUtil.getAppName()).stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}

	@Override
	public boolean matchIgnoreUrls(HttpServletRequest request) {
		if (CustomConfig.IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomConfig.Multiple.ALL_URLS.get(UaaUtil.getAppName()).stream().noneMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			throw new BizException(ResultCode.NOT_FOUND);
		}
		return false;
	}

	@Override
	public SecurityProperties.Jwt getJwt() {
		SecurityProperties.Jwt jwt = securityProperties.getJwt();
		jwt.setSignKey(RedisUtil.simple(UaaUtil.getAppName()) + jwt.getSignKey());
		return jwt;
	}

}

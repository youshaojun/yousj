package top.yousj.uaa.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BizException;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.config.CustomizeConfig;
import top.yousj.security.handler.CustomizeMatchHandler;
import top.yousj.security.matcher.CustomizeAntPathRequestMatcher;
import top.yousj.security.properties.SecurityProperties;
import top.yousj.security.holder.AppNameHolder;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class MultiServerMatchHandler implements CustomizeMatchHandler {

	private final SecurityProperties securityProperties;

	@Override
	public boolean matchAuthPermitUrls(HttpServletRequest request) {
		return CustomizeConfig.MultiServer.AUTH_PERMIT_URLS.get(AppNameHolder.get()).stream().anyMatch(url -> new CustomizeAntPathRequestMatcher(url).matches(request));
	}

	@Override
	public boolean matchIgnoreUrls(HttpServletRequest request) {
		if (CustomizeConfig.IGNORE_URLS.stream().anyMatch(url -> new CustomizeAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomizeConfig.MultiServer.SELF_IGNORE_URLS.get(AppNameHolder.get()).stream().anyMatch(url -> new CustomizeAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomizeConfig.MultiServer.ALL_URLS.get(AppNameHolder.get()).stream().noneMatch(url -> new CustomizeAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			throw new BizException(ResultCode.NOT_FOUND);
		}
		return false;
	}

	@Override
	public SecurityProperties.Jwt getJwt() {
		SecurityProperties.Jwt jwt = CustomizeConfig.MultiServer.JWT_CONFIG.getOrDefault(AppNameHolder.get(), securityProperties.getJwt());
		jwt.setSignKey(RedisUtil.simple(AppNameHolder.get()) + jwt.getSignKey());
		return jwt;
	}

}

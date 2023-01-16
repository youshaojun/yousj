package top.yousj.uaa.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BizException;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.config.CustomConfig;
import top.yousj.security.handler.CustomMatchHandler;
import top.yousj.security.matcher.CustomAntPathRequestMatcher;
import top.yousj.security.properties.SecurityProperties;
import top.yousj.security.holder.AppNameHolder;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class MultiServerMatchHandler implements CustomMatchHandler {

	private final SecurityProperties securityProperties;

	@Override
	public boolean matchAuthPermitUrls(HttpServletRequest request) {
		return CustomConfig.MultiServer.AUTH_PERMIT_URLS.get(AppNameHolder.get()).stream().anyMatch(url -> new CustomAntPathRequestMatcher(url).matches(request));
	}

	@Override
	public boolean matchIgnoreUrls(HttpServletRequest request) {
		if (CustomConfig.IGNORE_URLS.stream().anyMatch(url -> new CustomAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomConfig.MultiServer.SELF_IGNORE_URLS.get(AppNameHolder.get()).stream().anyMatch(url -> new CustomAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomConfig.MultiServer.ALL_URLS.get(AppNameHolder.get()).stream().noneMatch(url -> new CustomAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			throw new BizException(ResultCode.NOT_FOUND);
		}
		return false;
	}

	@Override
	public SecurityProperties.Jwt getJwt() {
		SecurityProperties.Jwt jwt = CustomConfig.MultiServer.JWT_CONFIG.getOrDefault(AppNameHolder.get(), securityProperties.getJwt());
		jwt.setSignKey(RedisUtil.simple(AppNameHolder.get()) + jwt.getSignKey());
		return jwt;
	}

}

package top.yousj.uaa.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import top.yousj.commons.enums.ResultCode;
import top.yousj.commons.exception.BizException;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.config.CustomizeConfig;
import top.yousj.security.handler.CustomizeMatchHandler;
import top.yousj.security.matcher.CustomizeAntPathRequestMatcher;
import top.yousj.security.properties.SecurityProperties;
import top.yousj.security.holder.AppNameHolder;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class UaaMatchHandler implements CustomizeMatchHandler {

	private final SecurityProperties securityProperties;

	@Override
	public boolean matchAuthPermitUrls(HttpServletRequest request) {
		return CustomizeConfig.Uaa.AUTH_PERMIT_URLS.get(AppNameHolder.get()).stream().anyMatch(url -> new CustomizeAntPathRequestMatcher(url).matches(request));
	}

	@Override
	public boolean matchIgnoreUrls(HttpServletRequest request) {
		if (CustomizeConfig.IGNORE_URLS.stream().anyMatch(url -> new CustomizeAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomizeConfig.Uaa.SELF_IGNORE_URLS.get(AppNameHolder.get()).stream().anyMatch(url -> new CustomizeAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			return true;
		}
		if (CustomizeConfig.Uaa.ALL_URLS.get(AppNameHolder.get()).stream().noneMatch(url -> new CustomizeAntPathRequestMatcher(url, request.getMethod()).matches(request))) {
			throw new BizException(ResultCode.NOT_FOUND);
		}
		return false;
	}

	@Override
	public SecurityProperties.Jwt getJwt() {
		SecurityProperties.Jwt jwt = CustomizeConfig.Uaa.JWT_CONFIG.getOrDefault(AppNameHolder.get(), securityProperties.getJwt());
		jwt.setSignKey(RedisUtil.simple(AppNameHolder.get()) + jwt.getSignKey());
		return jwt;
	}

}

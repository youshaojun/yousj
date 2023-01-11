package top.yousj.security.handler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BizException;
import top.yousj.core.utils.SpringUtil;
import top.yousj.security.config.CustomConfig;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @author yousj
 * @since 2023-01-11
 */
@ConditionalOnMissingBean(CustomMatchRequestHandler.class)
public class AloneMatchRequestHandler implements CustomMatchRequestHandler {

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

	@PostConstruct
	public void initAloneConfig() {
		CustomConfig.Alone.ALL_URLS.addAll(SpringUtil.getMappingUrls());
		CustomConfig.Alone.AUTH_PERMIT_URLS.add("/logout");
	}

}

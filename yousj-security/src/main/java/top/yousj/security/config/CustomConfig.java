package top.yousj.security.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.CollectionUtils;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BusinessException;
import top.yousj.core.utils.ParamAssertUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author yousj
 * @since 2023-01-06
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CustomConfig {

	public static Set<String> COMMON_IGNORE_URLS = Sets.newConcurrentHashSet();

	static {
		COMMON_IGNORE_URLS.add("/reload/updateClass");
		COMMON_IGNORE_URLS.add("/reload/updateMapperXml");
		COMMON_IGNORE_URLS.add("/actuator/**");
		COMMON_IGNORE_URLS.add("/favicon.ico");
		COMMON_IGNORE_URLS.add("/doc.html");
		COMMON_IGNORE_URLS.add("/swagger-ui.html");
		COMMON_IGNORE_URLS.add("/css/**");
		COMMON_IGNORE_URLS.add("/js/**");
		COMMON_IGNORE_URLS.add("/docs/**");
		COMMON_IGNORE_URLS.add("/webjars/**");
		COMMON_IGNORE_URLS.add("/v2/**");
		COMMON_IGNORE_URLS.add("/v3/**");
		COMMON_IGNORE_URLS.add("/swagger-resources/**");
		COMMON_IGNORE_URLS.add("/swagger-ui/**");
		COMMON_IGNORE_URLS.add("/druid/**");
	}

	/**
	 * 单项目自定义path过滤
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Alone {

		public static Set<String> ALL_URLS = Sets.newConcurrentHashSet();

		public static Set<String> AUTH_PERMIT_URLS = Sets.newConcurrentHashSet();

		public static Set<String> IGNORE_URLS = Sets.newConcurrentHashSet();

	}

	/**
	 * 多项目集中式自定义path过滤
	 */
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Multiple {

		public static Map<String, Set<String>> ALL_URLS = Maps.newConcurrentMap();

		public static Map<String, Set<String>> AUTH_PERMIT_URLS = Maps.newConcurrentMap();

		public static Map<String, Set<String>> IGNORE_URLS = Maps.newConcurrentMap();

	}

	public interface CustomMatchRequestHandler {

		boolean matchAuthPermitUrls(HttpServletRequest request);

		boolean matchIgnoreUrls(HttpServletRequest request);

	}

	@Bean
	@ConditionalOnMissingBean(CustomMatchRequestHandler.class)
	public CustomMatchRequestHandler aloneCustomHandler() {
		return new CustomMatchRequestHandler() {

			@Override
			public boolean matchAuthPermitUrls(HttpServletRequest request) {
				return !CollectionUtils.isEmpty(Alone.AUTH_PERMIT_URLS) && Alone.AUTH_PERMIT_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
			}

			@Override
			public boolean matchIgnoreUrls(HttpServletRequest request) {
				if (COMMON_IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
					return true;
				}
				ParamAssertUtil.notEmpty(Alone.ALL_URLS, "please initialize ALL_URLS.");
				if (Alone.ALL_URLS.stream().noneMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request))) {
					throw new BusinessException(ResultCode.NOT_FOUND);
				}
				return Alone.IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, request.getMethod()).matches(request));
			}
		};
	}

}

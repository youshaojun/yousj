package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsUtils;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.utils.ParamAssertUtil;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.filter.JwtAuthenticationFilter;
import top.yousj.security.utils.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

import static top.yousj.security.config.CustomConfig.*;

/**
 * @author yousj
 * @since 2023-01-02
 */
@Configuration
public class SecurityConfigAutoConfigure {

	@EnableWebSecurity
	@RequiredArgsConstructor
	@ConditionalOnMissingBean(WebSecurityConfigurerAdapterImport.class)
	@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
	public static class SecurityFilterChainConfigAutoConfigure {

		public final HttpSecurityConfig httpSecurityConfig;

		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			httpSecurityConfig.apply(http);
			return http.build();
		}

		@Bean
		public WebSecurityCustomizer webSecurityCustomizer() {
			return web -> web.ignoring().antMatchers(COMMON_IGNORE_URLS.toArray(new String[0]));
		}

	}

	/**
	 * SpringBoot低版本兼容
	 * 请升级SpringBoot版本使用{@link SecurityConfigAutoConfigure}
	 */
	@RequiredArgsConstructor
	public class WebSecurityConfigurerAdapterImport extends WebSecurityConfigurerAdapter {

		private final HttpSecurityConfig httpSecurityConfig;

		@Override
		public void configure(HttpSecurity http) throws Exception {
			httpSecurityConfig.apply(http);
			http.authorizeRequests().antMatchers(COMMON_IGNORE_URLS.toArray(new String[0])).permitAll();
		}

	}

	@Component
	@RequiredArgsConstructor
	public class HttpSecurityConfig {

		private final JwtAuthenticationFilter jwtAuthenticationFilter;
		private final UserDetailsService userDetailsService;
		private final SecurityExceptionAdviceHandler securityExceptionAdviceHandler;

		public void apply(HttpSecurity http) throws Exception {
			http
				// 关闭cors, csrf
				.cors().and().csrf().disable()
				// 关闭headers缓存
				.headers().cacheControl().disable()
				.and()
				.formLogin()
				.and()
				.authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				// 其余资源走自定义权限认证
				.anyRequest().access("@httpSecurityConfig.hasPermission(request)")
				.and()
				// 禁用session, 使用token方式认证
				.sessionManagement().sessionFixation().migrateSession().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				// 自定义登录认证逻辑
				.userDetailsService(userDetailsService)
				// 由实现类统一处理异常
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				// 自定义认证异常处理
				.exceptionHandling()
				// 已认证用户权限认证异常处理
				.accessDeniedHandler(((req, res, e) -> securityExceptionAdviceHandler.write(res, e)))
				// 未认证用户权限认证异常处理
				.authenticationEntryPoint(((req, res, e) -> securityExceptionAdviceHandler.write(res, e)));
		}

		public boolean hasPermission(HttpServletRequest request) {
			String appName = request.getHeader(UaaConstant.APP_NAME);
			ParamAssertUtil.notNull(appName, "app name can't be null.");
			List<String> urls = SecurityUtil.getAuthorities();
			Set<String> authPermitUrls = AUTH_PERMIT_URLS.get(appName);
			if (!CollectionUtils.isEmpty(authPermitUrls) && authPermitUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request))) {
				return true;
			}
			return urls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
		}

	}

}

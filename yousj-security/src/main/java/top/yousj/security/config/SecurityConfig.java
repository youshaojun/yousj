package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsUtils;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.utils.ParamAssertUtil;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.filter.JwtAuthenticationFilter;
import top.yousj.security.utils.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static top.yousj.security.config.CustomConfig.*;

/**
 * @author yousj
 * @since 2023-01-02
 */
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnBean(SecurityFilterChain.class)
@AutoConfiguration(after = SecurityExceptionAdviceHandler.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	private final SecurityExceptionAdviceHandler securityExceptionAdviceHandler;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsService userDetailsService;

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpFirewall httpFirewall() {
		return new StrictHttpFirewall();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
			.anyRequest().access("@securityConfig.hasPermission(request)")
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
			.accessDeniedHandler(((req, res, e) -> securityExceptionAdviceHandler.write(ResultCode.ACCESS_DENIED)))
			// 未认证用户权限认证异常处理
			.authenticationEntryPoint(((req, res, e) -> securityExceptionAdviceHandler.write(ResultCode.UNAUTHORIZED)));

		return http.build();
	}

	public boolean hasPermission(HttpServletRequest request) {
		String appName = request.getHeader(UaaConstant.APP_NAME);
		ParamAssertUtil.notNull(appName, "app name is null.");
		List<String> urls = SecurityUtil.getAuthorities();
		Set<String> authPermitUrls = AUTH_PERMIT_URLS.get(appName);
		if (!CollectionUtils.isEmpty(authPermitUrls) && authPermitUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request))) {
			return true;
		}
		return urls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}

}

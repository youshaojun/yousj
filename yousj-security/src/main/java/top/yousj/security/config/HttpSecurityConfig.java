package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import top.yousj.commons.enums.ResultCode;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.filter.JwtAuthenticationFilter;
import top.yousj.security.properties.SecurityProperties;

@Component
@RequiredArgsConstructor
public class HttpSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsService userDetailsService;
	private final SecurityExceptionAdviceHandler securityExceptionAdviceHandler;
	private final SecurityProperties securityProperties;

	public void apply(HttpSecurity http) throws Exception {
		http
			// 关闭cors, csrf
			.cors().and().csrf().disable()
			// 关闭headers缓存
			.headers().cacheControl().disable()
			.and()
			.formLogin()
			// 关于动态加载配置(过于复杂) https://docs.spring.io/spring-security/site/docs/4.2.4.RELEASE/reference/htmlsingle/#appendix-faq-dynamic-url-metadata
			// 全部由JwtAuthenticationFilter处理, 可动态加载配置
			// TODO 考虑使用[sureness](https://github.com/dromara/sureness/blob/master/README_CN.md)代替security
			//.and()
			//.authorizeRequests()
			//.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
			// .anyRequest().access("@rbacAuthorityService.hasPermission(request,authentication)")
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
			.accessDeniedHandler(((req, res, e) -> securityExceptionAdviceHandler.write(res, ResultCode.ACCESS_DENIED)))
			// 未认证用户权限认证异常处理
			.authenticationEntryPoint(((req, res, e) -> securityExceptionAdviceHandler.write(res, ResultCode.UNAUTHORIZED)));
	}

	public String[] getAntMatchers() {
		return securityProperties.isDynamicConfiguration() ? new String[]{"/**"} : CustomizeConfig.IGNORE_URLS.toArray(new String[]{});
	}

}

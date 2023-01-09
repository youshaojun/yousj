package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import top.yousj.security.filter.JwtAuthenticationFilter;

/**
 * SpringBoot低版本兼容
 * 请升级SpringBoot版本使用{@link SecurityFilterChainConfigAutoConfigure}做权限控制
 */
@RequiredArgsConstructor
public class WebSecurityConfigurerAdapterImport extends WebSecurityConfigurerAdapter {

	private final HttpSecurityConfig httpSecurityConfig;

	/**
	 * 放行所有请求, 认证由 {@link JwtAuthenticationFilter} 处理
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/**").permitAll();
		httpSecurityConfig.apply(http);
	}

}
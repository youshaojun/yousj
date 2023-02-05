package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SpringBoot低版本兼容
 * 请升级SpringBoot版本使用{@link SecurityFilterChainConfigAutoConfigure}做权限控制
 */
@RequiredArgsConstructor
public class WebSecurityConfigurerAdapterImport extends WebSecurityConfigurerAdapter {

	private final HttpSecurityConfig httpSecurityConfig;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(httpSecurityConfig.getAntMatchers()).permitAll();
		httpSecurityConfig.apply(http);
	}

}
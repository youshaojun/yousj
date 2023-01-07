package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static top.yousj.security.config.CustomConfig.COMMON_IGNORE_URLS;

/**
 * SpringBoot低版本兼容
 * 请升级SpringBoot版本使用{@link SecurityFilterChainConfigAutoConfigure}
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

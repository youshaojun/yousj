package top.yousj.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import top.yousj.security.filter.JwtAuthenticationFilter;

/**
 * @author yousj
 * @since 2023-01-02
 */
@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnMissingBean(WebSecurityConfigurerAdapterImport.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityFilterChainConfigAutoConfigure {

	public final HttpSecurityConfig httpSecurityConfig;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		httpSecurityConfig.apply(http);
		return http.build();
	}

	/**
	 * 放行所有请求交给 {@link JwtAuthenticationFilter} 和 {@link HttpSecurityConfig#hasPermission(javax.servlet.http.HttpServletRequest)}
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().antMatchers("/**");
	}

}

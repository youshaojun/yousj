package top.yousj.security.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.utils.ParamAssertUtil;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static top.yousj.security.config.CustomConfig.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnBean(SecurityFilterChain.class)
@AutoConfigureAfter(SecurityExceptionAdviceHandler.class)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;
	private final SecurityExceptionAdviceHandler securityExceptionAdviceHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) {
		try {
			if (COMMON_IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, httpServletRequest.getMethod()).matches(httpServletRequest))) {
				filterChain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
			String appName = httpServletRequest.getHeader(UaaConstant.APP_NAME);
			ParamAssertUtil.notNull(appName, "app name can't be null.");
			Set<String> ignoreUrls = IGNORE_URLS.get(appName);
			if (ignoreUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url, httpServletRequest.getMethod()).matches(httpServletRequest))) {
				filterChain.doFilter(httpServletRequest, httpServletResponse);
				return;
			}
			String jwtToken = JwtUtil.getJwtFromRequest(httpServletRequest);
			if (StringUtils.isBlank(jwtToken)) {
				throw new JwtException(StringUtils.EMPTY);
			}
			String subject = jwtUtil.paresJwtToken(jwtToken);
			UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(httpServletRequest, httpServletResponse);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			securityExceptionAdviceHandler.write(ResultCode.SYSTEM_ERROR);
		}
	}

}

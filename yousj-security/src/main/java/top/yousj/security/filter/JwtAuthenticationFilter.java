package top.yousj.security.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.utils.ParamAssertUtil;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

import static top.yousj.security.config.CustomConfig.*;

@Slf4j
@Component
@RequiredArgsConstructor
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
			Set<String> ignoreUrls = IGNORE_URLS.getOrDefault(appName, Collections.emptySet());
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
			securityExceptionAdviceHandler.write(httpServletResponse, e);
		}
	}

}

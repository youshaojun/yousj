package top.yousj.security.filter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.util.FieldUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.enums.ResultCode;
import top.yousj.security.handler.CustomMatchHandler;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.utils.JwtUtil;
import top.yousj.security.utils.SecurityUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final SecurityExceptionAdviceHandler adviceHandler;
	private final CustomMatchHandler customMatchHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		try {
			if (customMatchHandler.matchIgnoreUrls(request)) {
				filterChain.doFilter(request, response);
				return;
			}
			String jwtToken = JwtUtil.getJwtFromRequest(request);
			if (StringUtils.isBlank(jwtToken)) {
				throw new JwtException(StringUtils.EMPTY);
			}
			String subject = JwtUtil.paresJwtToken(jwtToken);
			UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			setUserIdHeader(response, userDetails);
			if (!hasPermission(request)) {
				adviceHandler.write(response, ResultCode.ACCESS_DENIED);
				return;
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			adviceHandler.write(response, adviceHandler.handle(e));
		}
	}

	private void setUserIdHeader(HttpServletResponse response, UserDetails userDetails) {
		try {
			response.setHeader(UaaConstant.FORWARD_AUTH_HEADER_USER_ID, String.valueOf(FieldUtils.getFieldValue(userDetails, "id")));
		} catch (Exception ignored) {
		}
	}

	private boolean hasPermission(HttpServletRequest request) {
		if (customMatchHandler.matchAuthPermitUrls(request)) {
			return true;
		}
		return SecurityUtil.getAuthorities().stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
	}

}

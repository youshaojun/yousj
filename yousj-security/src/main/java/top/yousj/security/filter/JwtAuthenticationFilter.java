package top.yousj.security.filter;

import com.google.common.collect.Sets;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import top.yousj.security.utils.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	public static Set<String> IGNORE_URLS = Sets.newHashSet();

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
		if (IGNORE_URLS.stream().anyMatch(url -> new AntPathRequestMatcher(url, httpServletRequest.getMethod()).matches(httpServletRequest))) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}
		String jwtToken = JwtUtil.getJwtFromRequest(httpServletRequest);
		if (StringUtils.isBlank(jwtToken)) throw new JwtException(StringUtils.EMPTY);
		String subject = jwtUtil.paresJwtToken(jwtToken);
		UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

}

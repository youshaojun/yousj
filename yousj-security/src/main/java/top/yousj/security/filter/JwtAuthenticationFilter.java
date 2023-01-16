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
import top.yousj.core.entity.R;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.utils.UaaUtil;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.handler.CustomMatchHandler;
import top.yousj.security.exception.SecurityExceptionAdviceHandler;
import top.yousj.security.matcher.CustomAntPathRequestMatcher;
import top.yousj.security.properties.SecurityProperties;
import top.yousj.security.utils.AppNameHolder;
import top.yousj.security.utils.JwtUtil;
import top.yousj.security.utils.SecurityUtil;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final SecurityExceptionAdviceHandler adviceHandler;
	private final CustomMatchHandler customMatchHandler;
	private final SecurityProperties securityProperties;

	private static final String CACHE_USER_DETAILS_KEY = "userDetails";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		try {
			AppNameHolder.set(UaaUtil.getAppName(request, securityProperties.isUaa()));
			if (customMatchHandler.matchIgnoreUrls(request)) {
				if (securityProperties.isUaa()) {
					adviceHandler.write(response, R.ok());
					return;
				}
				filterChain.doFilter(request, response);
				return;
			}
			String jwtToken = JwtUtil.getJwtFromRequest(request);
			if (StringUtils.isBlank(jwtToken)) {
				throw new JwtException(StringUtils.EMPTY);
			}
			UserDetails userDetails = getUserDetails(jwtToken);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// 设置uaaUid
			setUaaUid(request, response, String.valueOf(JwtUtil.getUaaUid(jwtToken)));
			// 鉴权
			if (!hasPermission(request)) {
				adviceHandler.write(response, ResultCode.ACCESS_DENIED);
				return;
			}
			// 如果是uaa, 鉴权通过直接return
			if (securityProperties.isUaa()) {
				adviceHandler.write(response, R.ok());
				return;
			}
			// 不是uaa, 继续执行过滤器链
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			adviceHandler.write(response, adviceHandler.handle(e));
		} finally {
			AppNameHolder.clear();
		}
	}

	private UserDetails getUserDetails(String jwtToken) {
		String key = RedisUtil.simple(AppNameHolder.get(), CACHE_USER_DETAILS_KEY, JwtUtil.getUaaUid(jwtToken));
		Supplier<UserDetails> valueSupplier = () -> userDetailsService.loadUserByUsername(JwtUtil.paresJwtToken(jwtToken));
		Long userDetailsTtl = customMatchHandler.getJwt().getUserDetailsTtl();
		return RedisUtil.put(key, valueSupplier, userDetailsTtl);
	}

	private void setUaaUid(HttpServletRequest request, HttpServletResponse response, String uid) {
		try {
			response.setHeader(UaaConstant.FORWARD_AUTH_HEADER_USER_ID, uid);
			Map<String, String[]> parameterMap = request.getParameterMap();
			Method method = parameterMap.getClass().getMethod("setLocked", Boolean.class);
			method.invoke(parameterMap, Boolean.FALSE);
			parameterMap.put(UaaConstant.UID, new String[]{uid});
			method.invoke(parameterMap, Boolean.TRUE);
		} catch (Exception ignored) {
		}
	}

	private boolean hasPermission(HttpServletRequest request) {
		if (customMatchHandler.matchAuthPermitUrls(request)) {
			return true;
		}
		return SecurityUtil.getAuthorities().stream().anyMatch(url -> securityProperties.isUaa() ?
			new CustomAntPathRequestMatcher(url).matches(request) : new AntPathRequestMatcher(url).matches(request));
	}


}

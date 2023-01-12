package top.yousj.security.utils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Component;
import top.yousj.core.constant.StrPool;
import top.yousj.core.constant.UaaConstant;
import top.yousj.core.utils.DateUtil;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.handler.CustomMatchHandler;
import top.yousj.security.properties.SecurityProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yousj
 * @since 2023-01-02
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private static CustomMatchHandler customMatchHandler;

	@Autowired
	public JwtUtil(CustomMatchHandler customMatchHandler) {
		JwtUtil.customMatchHandler = customMatchHandler;
	}

	public static String createJwtToken(String username) {
		SecurityProperties.Jwt jwt = customMatchHandler.getJwt();
		Date date = new Date();
		JwtBuilder builder = Jwts.builder()
			.setSubject(username)
			.setIssuedAt(date)
			.signWith(SignatureAlgorithm.HS256, jwt.getSignKey())
			.claim("uuid", UUID.randomUUID().toString());
		builder.setExpiration(DateUtil.forever());
		String jwtToken = builder.compact();
		RedisUtil.put(jwt.getSignKey() + username, jwtToken, jwt.getExpire(), TimeUnit.MILLISECONDS);
		return jwtToken;
	}

	public static String paresJwtToken(String jwtToken) {
		String subject = getSubject(jwtToken);
		SecurityProperties.Jwt jwt = customMatchHandler.getJwt();
		String key = jwt.getSignKey() + subject;
		Object v = Optional.ofNullable(RedisUtil.get(key)).orElseThrow(() -> new AccountExpiredException(StrPool.EMPTY));
		if(jwt.isRenewal()){
			RedisUtil.put(key, v, jwt.getExpire(), TimeUnit.MILLISECONDS);
		}
		return subject;
	}

	public static String getSubject(String jwtToken) {
		return Jwts.parser().setSigningKey(customMatchHandler.getJwt().getSignKey()).parseClaimsJws(jwtToken).getBody().getSubject();
	}

	public static Boolean removeToken(String subject) {
		return RedisUtil.del(customMatchHandler.getJwt().getSignKey() + subject);
	}

	public static String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getParameter(UaaConstant.TOKEN_HEADER);
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(StrPool.BEARER)) {
			return bearerToken.substring(7);
		}
		if (StringUtils.isNotBlank(bearerToken)) {
			return bearerToken;
		}
		bearerToken = request.getHeader(UaaConstant.AUTHORIZATION);
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(StrPool.BEARER)) {
			return bearerToken.substring(7);
		}
		return bearerToken;
	}

}

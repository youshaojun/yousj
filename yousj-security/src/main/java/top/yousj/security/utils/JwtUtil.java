package top.yousj.security.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.stereotype.Component;
import top.yousj.commons.constant.StrPool;
import top.yousj.commons.constant.UaaConstant;
import top.yousj.commons.utils.DateUtil;
import top.yousj.commons.utils.FuncUtil;
import top.yousj.redis.utils.RedisUtil;
import top.yousj.security.handler.CustomizeMatchHandler;
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

	private static CustomizeMatchHandler customizeMatchHandler;

	@Autowired
	public JwtUtil(CustomizeMatchHandler customizeMatchHandler) {
		JwtUtil.customizeMatchHandler = customizeMatchHandler;
	}

	public static String createJwtToken(String username, Integer uid) {
		SecurityProperties.Jwt jwt = customizeMatchHandler.getJwt();
		Date date = new Date();
		JwtBuilder builder = Jwts.builder()
			.setId(UUID.randomUUID().toString())
			.setSubject(username)
			.setIssuedAt(date)
			.signWith(SignatureAlgorithm.HS256, jwt.getSignKey())
			.claim(UaaConstant.UID, uid);
		builder.setExpiration(DateUtil.forever());
		String jwtToken = builder.compact();
		FuncUtil.run(jwt.isRenewal(), ()->RedisUtil.put(jwt.getSignKey() + username, jwtToken, jwt.getTtl(), TimeUnit.MILLISECONDS));
		return jwtToken;
	}

	public static String paresJwtToken(String jwtToken) {
		String subject = getSubject(jwtToken);
		SecurityProperties.Jwt jwt = customizeMatchHandler.getJwt();
		if (!jwt.isRenewal()) {
			return subject;
		}
		String key = jwt.getSignKey() + subject;
		Object v = Optional.ofNullable(RedisUtil.get(key)).orElseThrow(() -> new AccountExpiredException(StrPool.EMPTY));
		RedisUtil.put(key, v, jwt.getTtl(), TimeUnit.MILLISECONDS);
		return subject;
	}

	public static String getSubject(String jwtToken) {
		return getBody(jwtToken).getSubject();
	}

	public static Integer getUaaUid(String jwtToken) {
		return getBody(jwtToken).get(UaaConstant.UID, Integer.class);
	}

	private static Claims getBody(String jwtToken) {
		return Jwts.parser().setSigningKey(customizeMatchHandler.getJwt().getSignKey()).parseClaimsJws(jwtToken).getBody();
	}

	public static Boolean removeToken(String subject) {
		return RedisUtil.remove(customizeMatchHandler.getJwt().getSignKey() + subject);
	}

	public static Boolean removeToken(HttpServletRequest request) {
		return removeToken(getSubject(getJwtFromRequest(request)));
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

package top.yousj.security.exception;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.entity.R;
import top.yousj.core.exception.AbstractExceptionAdviceHandler;
import top.yousj.core.exception.BizException;
import top.yousj.security.properties.SecurityProperties;

@Slf4j
@Component
public class SecurityExceptionAdviceHandler extends AbstractExceptionAdviceHandler {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public boolean isHttpStatus() {
		return securityProperties.isHttpStatus();
	}

	@Override
	public R<String> handle(Exception ex) {
		if (ex instanceof BizException) {
			return R.fail(((BizException) ex).getCode(), ex.getMessage());
		}
		if (ex instanceof BadCredentialsException || ex instanceof UsernameNotFoundException) {
			return R.fail(ResultCode.USERNAME_NOT_FOUND);
		}
		if (ex instanceof AccessDeniedException) {
			return R.fail(ResultCode.ACCESS_DENIED);
		}
		if (ex instanceof AuthenticationException) {
			return R.fail(ResultCode.UNAUTHORIZED);
		}
		if (ex instanceof JwtException) {
			return R.fail(ResultCode.TOKEN_PARSER_FAIL);
		}
		return null;
	}

}

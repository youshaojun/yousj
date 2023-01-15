package top.yousj.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.constant.StrPool;
import top.yousj.core.entity.R;
import top.yousj.core.exception.BizException;
import top.yousj.core.exception.ExceptionAdviceHandler;
import top.yousj.security.properties.SecurityProperties;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityExceptionAdviceHandler implements ExceptionAdviceHandler {

	private final ObjectMapper objectMapper;
	private final SecurityProperties securityProperties;

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

	public void write(HttpServletResponse response, ResultCode resultCode) {
		write(response, R.fail(resultCode));
	}

	@SneakyThrows
	public void write(HttpServletResponse response, R<String> r) {
		r = Objects.nonNull(r) ? r : R.fail(ResultCode.SYSTEM_ERROR);
		response.setCharacterEncoding(StrPool.CHARSET_NAME);
		response.setStatus(securityProperties.isHttpStatus() ? r.getCode() : HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setHeader("aaaaa", "失败啦");
		PrintWriter writer = response.getWriter();
		writer.write(objectMapper.writeValueAsString(r));
		writer.flush();
		writer.close();
	}

}

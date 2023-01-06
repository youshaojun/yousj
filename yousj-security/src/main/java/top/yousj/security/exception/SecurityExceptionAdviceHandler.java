package top.yousj.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.constant.StrPool;
import top.yousj.core.entity.R;
import top.yousj.core.exception.ExceptionAdviceHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RequiredArgsConstructor
@ConditionalOnBean(SecurityFilterChain.class)
public class SecurityExceptionAdviceHandler implements ExceptionAdviceHandler {

	private final HttpServletResponse httpServletResponse;
	private final ObjectMapper objectMapper;

	@Override
	public R<String> handle(Exception ex) {
		if (ex instanceof AccountExpiredException) {
			return R.failure(ResultCode.UNAUTHORIZED);
		}
		if (ex instanceof UsernameNotFoundException) {
			return R.failure(ResultCode.USERNAME_NOT_FOUND);
		}
		if (ex instanceof JwtException) {
			return R.failure(ResultCode.TOKEN_PARSER_FAIL);
		}
		return null;
	}

	@SneakyThrows
	public void write(ResultCode resultCode) {
		httpServletResponse.setCharacterEncoding(StrPool.CHARSET_NAME);
		httpServletResponse.setStatus(HttpStatus.OK.value());
		httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = httpServletResponse.getWriter();
		writer.write(objectMapper.writeValueAsString(R.failure(resultCode)));
		writer.flush();
		writer.close();
	}

}

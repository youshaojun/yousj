package top.yousj.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.constant.StrPool;
import top.yousj.core.entity.R;
import top.yousj.core.exception.BusinessException;
import top.yousj.core.exception.ExceptionAdviceHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class SecurityExceptionAdviceHandler implements ExceptionAdviceHandler {

	private final ObjectMapper objectMapper;

	@Override
	public R<String> handle(Exception ex) {
		if (ex instanceof BusinessException) {
			return R.failure(((BusinessException) ex).getCode(), ex.getMessage());
		}
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
	public void write(HttpServletResponse response, Exception ex) {
		response.setCharacterEncoding(StrPool.CHARSET_NAME);
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		R<String> r = handle(ex);
		writer.write(objectMapper.writeValueAsString(Objects.nonNull(r) ? r : R.failure(ResultCode.SYSTEM_ERROR)));
		writer.flush();
		writer.close();
	}

}

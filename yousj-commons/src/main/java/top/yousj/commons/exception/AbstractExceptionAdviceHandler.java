package top.yousj.commons.exception;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import top.yousj.commons.constant.StrPool;
import top.yousj.commons.entity.R;
import top.yousj.commons.enums.ResultCode;
import top.yousj.commons.utils.JsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @author yousj
 * @since 2023-01-16
 */
public abstract class AbstractExceptionAdviceHandler implements ExceptionAdviceHandler {

	public boolean isHttpStatus() {
		return false;
	}

	public void write(HttpServletResponse response, ResultCode resultCode) {
		write(response, R.fail(resultCode));
	}

	@SneakyThrows
	public void write(HttpServletResponse response, R<String> r) {
		r = Objects.nonNull(r) ? r : R.fail(ResultCode.SYSTEM_ERROR);
		response.setCharacterEncoding(StrPool.CHARSET_NAME);
		response.setStatus(isHttpStatus() ? r.getCode() : HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(JsonUtil.toJson(r));
		writer.flush();
		writer.close();
	}

}

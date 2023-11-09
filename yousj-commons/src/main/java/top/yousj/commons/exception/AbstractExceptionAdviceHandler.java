package top.yousj.commons.exception;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import top.yousj.commons.entity.R;
import top.yousj.commons.enums.ResultCode;
import top.yousj.commons.utils.WebUtil;

import javax.servlet.http.HttpServletResponse;
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
        WebUtil.renderJson(response, r, isHttpStatus() ? r.getCode() : HttpStatus.OK.value());
	}

}

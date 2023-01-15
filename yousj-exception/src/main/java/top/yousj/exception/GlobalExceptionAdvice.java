package top.yousj.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.yousj.core.entity.R;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.ExceptionAdviceHandler;

import java.util.List;
import java.util.Objects;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionAdvice implements Ordered {

	private final List<ExceptionAdviceHandler> exceptionAdviceHandlers;

	@Override
	public int getOrder() {
		return 0;
	}

	@ExceptionHandler(Exception.class)
	public R<String> exceptionHandler(Exception ex) {
		for (ExceptionAdviceHandler exceptionAdviceHandler : exceptionAdviceHandlers) {
			R<String> r = exceptionAdviceHandler.handle(ex);
			if (Objects.nonNull(r)) {
				return r;
			}
		}
		log.error(ex.getMessage(), ex);
		if (ex instanceof RuntimeException) {
			return R.fail(ResultCode.SYSTEM_ERROR);
		}
		return R.fail(ResultCode.SYSTEM_ERROR);
	}

}

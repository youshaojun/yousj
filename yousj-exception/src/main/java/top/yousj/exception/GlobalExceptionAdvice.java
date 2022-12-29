package top.yousj.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.yousj.core.entity.R;
import top.yousj.core.entity.ResultCode;
import top.yousj.core.exception.BusinessException;

import java.util.stream.Collectors;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionAdvice implements Ordered {

	@Override
	public int getOrder() {
		return 0;
	}

	@ExceptionHandler(Exception.class)
	public R<String> exceptionHandler(Exception ex) {

		if (ex instanceof BusinessException) {
			return R.failure(((BusinessException) ex).getCode(), ex.getMessage());
		}
		if (ex instanceof NoHandlerFoundException) {
			return R.failure(ResultCode.NOT_FOUND);
		}
		if (ex instanceof HttpRequestMethodNotSupportedException) {
			return R.failure(ResultCode.UNSUPPORTED_METHOD_TYPE);
		}
		if (ex instanceof HttpMediaTypeNotAcceptableException || ex instanceof HttpMediaTypeNotSupportedException) {
			return R.failure(ResultCode.UNSUPPORTED_MEDIA_TYPE);
		}
		if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
			return R.failure(ResultCode.PARAM_NOT_MATCH, e.getBindingResult()
				.getAllErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(",")));
		}
		if (ex instanceof BindException) {
			BindException e = (BindException) ex;
			return R.failure(ResultCode.PARAM_NOT_MATCH, e.getFieldErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(",")));
		}
		if (ex instanceof MissingServletRequestParameterException
			|| ex instanceof HttpMessageNotReadableException
			|| ex instanceof IllegalArgumentException
			|| ex instanceof MethodArgumentTypeMismatchException
			|| ex instanceof ConversionFailedException
			|| ex instanceof IllegalStateException
		) {
			return R.failure(ResultCode.PARAM_NOT_MATCH);
		}
		log.error(ex.getMessage(), ex);
		if (ex instanceof RuntimeException) {
			return R.failure(ResultCode.SYSTEM_ERROR);
		}
		return R.failure(ResultCode.SYSTEM_ERROR);
	}

}

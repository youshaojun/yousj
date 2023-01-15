package top.yousj.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.yousj.core.entity.R;
import top.yousj.core.enums.ResultCode;
import top.yousj.core.exception.BizException;
import top.yousj.core.exception.ExceptionAdviceHandler;

import java.util.stream.Collectors;

/**
 * @author yousj
 * @since 2023-01-11
 */
@Component
public class DefaultExceptionAdviceHandler implements ExceptionAdviceHandler {

	@Override
	public R<String> handle(Exception ex) {
		if (ex instanceof BizException) {
			return R.fail(((BizException) ex).getCode(), ex.getMessage());
		}
		if (ex instanceof NoHandlerFoundException) {
			return R.fail(ResultCode.NOT_FOUND);
		}
		if (ex instanceof HttpRequestMethodNotSupportedException) {
			return R.fail(ResultCode.UNSUPPORTED_METHOD_TYPE);
		}
		if (ex instanceof HttpMediaTypeNotAcceptableException || ex instanceof HttpMediaTypeNotSupportedException) {
			return R.fail(ResultCode.UNSUPPORTED_MEDIA_TYPE);
		}
		if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
			return R.fail(ResultCode.PARAM_NOT_MATCH, e.getBindingResult()
				.getAllErrors()
				.stream()
				.map(DefaultMessageSourceResolvable::getDefaultMessage)
				.collect(Collectors.joining(",")));
		}
		if (ex instanceof BindException) {
			BindException e = (BindException) ex;
			return R.fail(ResultCode.PARAM_NOT_MATCH, e.getFieldErrors()
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
			return R.fail(ResultCode.PARAM_NOT_MATCH);
		}
		return null;
	}
}

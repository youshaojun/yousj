package top.yousj.crpyto.advice;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import top.yousj.core.constant.StrPool;
import top.yousj.crpyto.annotation.Decrypt;
import top.yousj.crpyto.config.KeyPropertiesHolder;
import top.yousj.crpyto.handler.CryptHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author yousj
 * @since 2023-01-06
 */
@ControllerAdvice
@RequiredArgsConstructor
public class DecryptReqAdvice extends RequestBodyAdviceAdapter {

	private final KeyPropertiesHolder keyPropertiesHolder;
	private final HttpServletRequest request;

	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasMethodAnnotation(Decrypt.class) || methodParameter.hasParameterAnnotation(Decrypt.class);
	}

	@Override
	public HttpInputMessage beforeBodyRead(final HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return new HttpInputMessage() {
			@SneakyThrows
			@Override
			public InputStream getBody() {
				Class<? extends CryptHandler> cryptHandlerClazz = methodParameter.getMethodAnnotation(Decrypt.class).handler();
				CryptHandler cryptHandlerInstance = cryptHandlerClazz.getDeclaredConstructor().newInstance();
				byte[] body = new byte[inputMessage.getBody().available()];
				inputMessage.getBody().read(body);
				String decryptBody = cryptHandlerInstance.decrypt(new String(body, StrPool.CHARSET_NAME), keyPropertiesHolder.getKeyProperties(request));
				return new ByteArrayInputStream(decryptBody.getBytes(StrPool.CHARSET_NAME));
			}

			@Override
			public HttpHeaders getHeaders() {
				return inputMessage.getHeaders();
			}
		};
	}

}

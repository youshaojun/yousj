package top.yousj.crpyto.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.yousj.crpyto.annotation.Encrypt;
import top.yousj.crpyto.config.KeyPropertiesHolder;
import top.yousj.crpyto.handler.CryptHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author yousj
 * @since 2023-01-06
 */
@ControllerAdvice
@RequiredArgsConstructor
public class EncryptResAdvice implements ResponseBodyAdvice<Object> {

	private final KeyPropertiesHolder keyPropertiesHolder;
	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasMethodAnnotation(Encrypt.class);
	}

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		Class<? extends CryptHandler> cryptHandlerClazz = methodParameter.getMethodAnnotation(Encrypt.class).handler();
		CryptHandler cryptHandlerInstance = cryptHandlerClazz.getDeclaredConstructor().newInstance();
		if (Objects.isNull(body)) {
			return null;
		}
		return cryptHandlerInstance.encrypt(objectMapper.writeValueAsString(body), keyPropertiesHolder.getKeyProperties((HttpServletRequest) request));
	}
}

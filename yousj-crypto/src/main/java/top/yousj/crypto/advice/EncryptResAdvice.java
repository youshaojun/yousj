package top.yousj.crypto.advice;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.yousj.crypto.annotation.Encrypt;
import top.yousj.crypto.constant.PropertyConstant;
import top.yousj.crypto.converter.DataConverter;

/**
 * @author yousj
 * @since 2023-01-06
 */
@RequiredArgsConstructor
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
@ConditionalOnProperty(prefix = PropertyConstant.CRYPTO, name = "encrypt.enable", havingValue = "true", matchIfMissing = true)
public class EncryptResAdvice implements ResponseBodyAdvice<Object> {

	private final DataConverter dataConverter;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasMethodAnnotation(Encrypt.class);
	}

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		Encrypt encrypt = methodParameter.getMethodAnnotation(Encrypt.class);
		return dataConverter.convert(encrypt.handler(), true, encrypt.onlyData(), body);
	}
}

package top.yousj.crypto.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.yousj.core.utils.SpringUtil;
import top.yousj.crypto.annotation.Encrypt;
import top.yousj.crypto.config.KeyPropertiesHolder;
import top.yousj.crypto.handler.CryptHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author yousj
 * @since 2023-01-06
 */
@RequiredArgsConstructor
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
public class EncryptResAdvice implements ResponseBodyAdvice<Object> {

	private final HttpServletRequest httpServletRequest;
	private final KeyPropertiesHolder keyPropertiesHolder;
	private final ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
		return methodParameter.hasMethodAnnotation(Encrypt.class);
	}

	@SneakyThrows
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if (Objects.isNull(body)) {
			return null;
		}
		CryptHandler cryptHandler = SpringUtil.getBean(methodParameter.getMethodAnnotation(Encrypt.class).handler());
		return cryptHandler.encrypt(objectMapper.writeValueAsString(body), keyPropertiesHolder.getKeyProperties(httpServletRequest));
	}
}

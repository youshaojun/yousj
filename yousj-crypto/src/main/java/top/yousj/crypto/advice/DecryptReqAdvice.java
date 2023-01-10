package top.yousj.crypto.advice;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import top.yousj.core.constant.StrPool;
import top.yousj.crypto.annotation.Decrypt;
import top.yousj.crypto.constant.PropertyConstant;
import top.yousj.crypto.utils.AdviceCryptUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @author yousj
 * @since 2023-01-06
 */
@RequiredArgsConstructor
@RestControllerAdvice(annotations = {Controller.class, RestController.class})
@ConditionalOnProperty(prefix = PropertyConstant.CRYPTO, name = "decrypt.enable", havingValue = "true", matchIfMissing = true)
public class DecryptReqAdvice extends RequestBodyAdviceAdapter {

	private final AdviceCryptUtil adviceCryptUtil;

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
				byte[] body = new byte[inputMessage.getBody().available()];
				inputMessage.getBody().read(body);
				Decrypt decrypt = methodParameter.getMethodAnnotation(Decrypt.class);
				String decryptBody = adviceCryptUtil.handle(decrypt.handler(), false, decrypt.onlyData(), body);
				return new ByteArrayInputStream(decryptBody.getBytes(StrPool.CHARSET_NAME));
			}

			@Override
			public HttpHeaders getHeaders() {
				return inputMessage.getHeaders();
			}
		};
	}

}

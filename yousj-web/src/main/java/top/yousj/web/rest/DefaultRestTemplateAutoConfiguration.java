package top.yousj.web.rest;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import top.yousj.web.constant.PropertyConstant;
import top.yousj.web.properties.WebProperties;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yousj
 * @since 2023-01-30
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(WebProperties.class)
public class DefaultRestTemplateAutoConfiguration {

	private final WebProperties webProperties;

	@Bean
	@ConditionalOnMissingBean(name = "restTemplate")
	public RestOperations restTemplate() {
		return buildDefaultRestTemplate(false);
	}

	@Bean
	@ConditionalOnMissingBean(name = "proxyRestTemplate")
	@ConditionalOnProperty(prefix = PropertyConstant.WEB, name = "okhttp.proxy")
	public RestOperations proxyRestTemplate() {
		return buildDefaultRestTemplate(true);
	}

	private RestTemplate buildDefaultRestTemplate(boolean useProxy) {
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		httpClientBuilder
			.retryOnConnectionFailure(false)
			.connectTimeout(webProperties.getOkhttp().getConnectTimeout(), TimeUnit.SECONDS)
			.readTimeout(webProperties.getOkhttp().getReadTimeout(), TimeUnit.SECONDS)
			.writeTimeout(webProperties.getOkhttp().getWriteTimeout(), TimeUnit.SECONDS);
		if (useProxy) {
			setProxy(httpClientBuilder);
		}
		OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(httpClientBuilder.build());
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		addMessageConverters(restTemplate);
		return restTemplate;
	}

	private void setProxy(OkHttpClient.Builder httpClientBuilder) {
		WebProperties.Okhttp.Proxy proxy = webProperties.getOkhttp().getProxy();
		if (Objects.isNull(proxy)) {
			return;
		}

		httpClientBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getAddr(), proxy.getPort())));

		if (Objects.isNull(proxy.getUsername())) {
			return;
		}

		httpClientBuilder.proxyAuthenticator((route, response) -> {
			String credential = Credentials.basic(proxy.getUsername(), proxy.getPassword());
			return response.request().newBuilder()
				.header("Proxy-Authorization", credential)
				.build();
		});
	}

	private void addMessageConverters(RestTemplate restTemplate) {
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
		messageConverters.add(mappingJackson2HttpMessageConverter);
	}

}

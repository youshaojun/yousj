package top.yousj.rest.config;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import top.yousj.rest.properties.OkhttpProperties;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yousj
 * @since 2023-02-17
 */
public class RestConfigurer {

	public static RestTemplate createRestTemplate(boolean useProxy, OkhttpProperties okhttpProperties) {
		OkHttpClient.Builder httpClientBuilder = httpClientBuilder(okhttpProperties);
		if (useProxy) {
			setProxy(httpClientBuilder, okhttpProperties);
		}
		OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(httpClientBuilder.build());
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		addMessageConverters(restTemplate);
		return restTemplate;
	}

	public static OkHttpClient.Builder httpClientBuilder(OkhttpProperties okhttpProperties) {
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		httpClientBuilder
			.retryOnConnectionFailure(false)
			.connectTimeout(okhttpProperties.getConnectTimeout(), TimeUnit.SECONDS)
			.readTimeout(okhttpProperties.getReadTimeout(), TimeUnit.SECONDS)
			.writeTimeout(okhttpProperties.getWriteTimeout(), TimeUnit.SECONDS);
		return httpClientBuilder;
	}

	public static void setProxy(OkHttpClient.Builder httpClientBuilder, OkhttpProperties okhttpProperties) {
		OkhttpProperties.Proxy proxy = okhttpProperties.getProxy();
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

	public static void addMessageConverters(RestTemplate restTemplate) {
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_HTML));
		messageConverters.add(mappingJackson2HttpMessageConverter);
	}

}

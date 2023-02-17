package top.yousj.rest.config;

import lombok.Cleanup;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestOperations;
import top.yousj.commons.entity.R;
import top.yousj.commons.exception.BizException;
import top.yousj.commons.utils.FuncUtil;
import top.yousj.rest.properties.OkhttpProperties;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;

/**
 * @author yousj
 * @since 2023-02-17
 */
public class DynamicPoolProxy {

	private static RestOperations restTemplate;
	private static OkhttpProperties okhttpProperties;

	@Autowired
	public DynamicPoolProxy(RestOperations restTemplate, OkhttpProperties okhttpProperties) {
		DynamicPoolProxy.restTemplate = restTemplate;
		DynamicPoolProxy.okhttpProperties = okhttpProperties;
	}

	@Retryable
	public R<byte[]> call(String url, HttpMethod httpMethod, Map<String, String> params, String userAgent) {
		Objects.requireNonNull(url);
		return R.ok(call(url, httpMethod, null, params, userAgent, getProxy()));
	}

	@SneakyThrows
	private byte[] call(String url, HttpMethod httpMethod, String cookie, Map<String, String> params, String userAgent, OkhttpProperties.Proxy proxy) {
		OkHttpClient.Builder httpClientBuilder = RestConfigurer.httpClientBuilder(okhttpProperties);
		OkHttpClient okHttpClient = httpClientBuilder
			.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getAddr(), proxy.getPort())))
			.followRedirects(false)
			.build();
		Call call = okHttpClient.newCall(new Request.Builder()
			.url(url)
			.method(httpMethod.name(), getRequestBody(params))
			.headers(getAuthHeaders(cookie, proxy.getUsername(), proxy.getPassword(), userAgent))
			.build());
		@Cleanup Response execute = call.execute();
		if (execute.isSuccessful()) {
			return execute.body().bytes();
		} else if (execute.isRedirect()) {
			String setCookie = execute.header(HttpHeaders.SET_COOKIE);
			return call(url, httpMethod, setCookie, params, userAgent, proxy);
		}
		throw new BizException("代理请求失败");
	}

	public RequestBody getRequestBody(Map<String, String> params) {
		if (CollectionUtils.isEmpty(params)) {
			return null;
		}
		FormBody.Builder builder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			builder.add(entry.getKey(), entry.getValue());
		}
		return builder.build();
	}

	public OkhttpProperties.Proxy getProxy() {
		List<String> urls = okhttpProperties.getPool().getUrls();
		for (String url : urls) {
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
			String body = responseEntity.getBody();
			if (responseEntity.getStatusCode() != HttpStatus.OK || StringUtils.isEmpty(body)) {
				continue;
			}
			String[] proxyInfo = StringUtils.split(body, "@");
			if (proxyInfo.length == 2) {
				String[] account = StringUtils.split(proxyInfo[0], ":");
				String[] address = StringUtils.split(proxyInfo[1], ":");
				return new OkhttpProperties.Proxy()
					.setUsername(account[0])
					.setPassword(account[1])
					.setAddr(address[0])
					.setPort(Integer.valueOf(address[1]));
			}
		}
		throw new BizException("代理失效");
	}

	private Headers getAuthHeaders(String cookie, String acc, String pwd, String userAgent) {
		Headers.Builder builder = new Headers.Builder()
			.add(HttpHeaders.PROXY_AUTHORIZATION, Credentials.basic(acc, pwd))
			.add(HttpHeaders.USER_AGENT, StringUtils.defaultIfBlank(userAgent, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36"));
		FuncUtil.callIfNotNull(cookie, c -> builder.add(HttpHeaders.COOKIE, c));
		return builder.build();
	}

}

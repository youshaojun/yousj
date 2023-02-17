package top.yousj.rest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestOperations;
import top.yousj.rest.constant.PropertyConstant;
import top.yousj.rest.properties.OkhttpProperties;

/**
 * @author yousj
 * @since 2023-01-30
 */
@RequiredArgsConstructor
public class DefaultRestTemplateAutoConfiguration {

	private final OkhttpProperties okhttpProperties;

	@Bean
	@ConditionalOnMissingBean(name = "restTemplate")
	public RestOperations restTemplate() {
		return RestConfigurer.create(false, okhttpProperties);
	}

	@Bean
	@ConditionalOnMissingBean(name = "proxyRestTemplate")
	@ConditionalOnProperty(prefix = PropertyConstant.OKHTTP, name = "proxy")
	public RestOperations proxyRestTemplate() {
		return RestConfigurer.create(true, okhttpProperties);
	}

}

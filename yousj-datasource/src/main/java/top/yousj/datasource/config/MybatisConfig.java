package top.yousj.datasource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import top.yousj.datasource.interceptor.PerformanceInterceptor;

/**
 * @author yousj
 * @since 2023-01-03
 */
@Configuration(proxyBeanMethods = false)
public class MybatisConfig {

	@Bean
	@Profile({"dev", "test"})
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor().setWriteInLog(true);
	}

}

package top.yousj.datasource.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import top.yousj.datasource.interceptor.PerformanceInterceptor;

/**
 * @author yousj
 * @since 2023-01-03
 */
@Configuration
public class MybatisConfig {

	static {
		System.setProperty("druid.mysql.usePingMethod", Boolean.FALSE.toString());
	}

	@Bean
	@Profile({"dev", "test"})
	public PerformanceInterceptor performanceInterceptor() {
		return new PerformanceInterceptor();
	}

	@Bean
	@ConditionalOnMissingBean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		return mybatisPlusInterceptor;
	}

}

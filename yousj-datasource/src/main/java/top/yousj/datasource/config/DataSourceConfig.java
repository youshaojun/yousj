package top.yousj.datasource.config;

import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {

	static {
		System.setProperty("druid.mysql.usePingMethod", Boolean.FALSE.toString());
	}

}

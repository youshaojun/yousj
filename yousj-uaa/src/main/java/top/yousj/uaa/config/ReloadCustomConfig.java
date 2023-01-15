package top.yousj.uaa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static top.yousj.security.config.CustomConfig.IGNORE_URLS;

@EnableScheduling
@Configuration(proxyBeanMethods = false)
public class ReloadCustomConfig {

	static {
		IGNORE_URLS.add("/uaa/login");
		IGNORE_URLS.add("/uaa/logout");
	}


}

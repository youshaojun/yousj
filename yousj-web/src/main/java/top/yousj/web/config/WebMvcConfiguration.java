package top.yousj.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(
			"classpath:/static/");
		registry.addResourceHandler("/webjars/**").addResourceLocations(
			"classpath:/META-INF/resources/webjars/");

		registry.addResourceHandler("swagger-ui.html").addResourceLocations(
			"classpath:/META-INF/resources/");
		registry.addResourceHandler("doc.html").addResourceLocations(
			"classpath:/META-INF/resources/");

		super.addResourceHandlers(registry);
	}

}
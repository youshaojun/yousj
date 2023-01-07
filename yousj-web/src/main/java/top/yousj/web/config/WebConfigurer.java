package top.yousj.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

@Configuration
@EnableWebMvc
@ConditionalOnMissingBean(WebMvcConfigurer.class)
@ConditionalOnWebApplication
public class WebConfigurer implements WebMvcConfigurer {

	private final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(MappingJackson2HttpMessageConverter.class::isInstance);
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.simpleDateFormat(DEFAULT_DATE_FORMAT);
		builder.timeZone(TimeZone.getTimeZone("GMT+8"));
		builder.locale(Locale.SIMPLIFIED_CHINESE);
		converters.add(0, new MappingJackson2HttpMessageConverter(builder.build()));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
			.addResourceLocations(
				"classpath:/static/",
				"classpath:/public/",
				"classpath:/resources/",
				"classpath:/META-INF/resources/"
			);
		registry.addResourceHandler("doc.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}

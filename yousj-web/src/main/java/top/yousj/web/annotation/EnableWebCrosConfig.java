package top.yousj.web.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.web.config.CorsConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Import(CorsConfig.class)
public @interface EnableWebCrosConfig {
}

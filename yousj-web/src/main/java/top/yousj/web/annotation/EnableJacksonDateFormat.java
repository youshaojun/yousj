package top.yousj.web.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.web.config.JacksonConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JacksonConfiguration.class)
public @interface EnableJacksonDateFormat {

}
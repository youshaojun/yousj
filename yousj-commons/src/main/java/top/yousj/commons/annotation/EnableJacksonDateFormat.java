package top.yousj.commons.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.commons.config.jackson.JacksonConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JacksonConfiguration.class)
public @interface EnableJacksonDateFormat {

}
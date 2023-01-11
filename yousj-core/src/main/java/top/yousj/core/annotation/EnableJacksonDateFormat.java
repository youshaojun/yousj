package top.yousj.core.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.core.config.JacksonConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JacksonConfiguration.class)
public @interface EnableJacksonDateFormat {

}
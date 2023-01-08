package top.yousj.security.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.security.config.SecurityConfigAutoConfigure.WebSecurityConfigurerAdapterImport;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Import(WebSecurityConfigurerAdapterImport.class)
public @interface EnableWebSecurityConfigurerAdapter {
}

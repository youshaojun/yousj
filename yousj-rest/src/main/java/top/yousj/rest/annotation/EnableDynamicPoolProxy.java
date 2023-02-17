package top.yousj.rest.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.rest.config.DynamicPoolProxy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yousj
 * @since 2023-02-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(DynamicPoolProxy.class)
public @interface EnableDynamicPoolProxy {
}

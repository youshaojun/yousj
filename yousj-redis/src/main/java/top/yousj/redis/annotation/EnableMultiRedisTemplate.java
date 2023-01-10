package top.yousj.redis.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.redis.multi.MultiRedisBeanDefinitionRegistry;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(MultiRedisBeanDefinitionRegistry.class)
public @interface EnableMultiRedisTemplate {
}

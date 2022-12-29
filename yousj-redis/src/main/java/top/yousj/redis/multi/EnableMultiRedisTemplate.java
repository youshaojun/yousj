package top.yousj.redis.multi;

import org.springframework.context.annotation.Import;

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

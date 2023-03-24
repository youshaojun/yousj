package top.yousj.commons.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.commons.config.timecost.TimeCostBeanPostProcessor;
import top.yousj.commons.config.timecost.TimeCostSpringbootRunner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yousj
 * @since 2022-12-29
 * deprecated 参考 https://github.com/microsphere-projects/microsphere-spring-projects/blob/main/microsphere-spring/microsphere-spring-context/src/main/java/io/github/microsphere/spring/context/event/
 */
@Retention(RetentionPolicy.RUNTIME)
@Import({TimeCostBeanPostProcessor.class, TimeCostSpringbootRunner.class})
@Deprecated
public @interface EnableTimeCostStatistics {
}

package top.yousj.commons.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.commons.config.timecost.TimeCostBeanPostProcessor;
import top.yousj.commons.config.timecost.TimeCostSpringbootRunner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yousj
 * @since 2022-12-29
 * deprecated 参考 https://github.com/microsphere-projects/microsphere-spring/blob/main/microsphere-spring-context/src/main/java/io/microsphere/spring/context/event/BeanTimeStatistics.java
 */
@Retention(RetentionPolicy.RUNTIME)
@Import({TimeCostBeanPostProcessor.class, TimeCostSpringbootRunner.class})
@Deprecated
public @interface EnableTimeCostStatistics {
}

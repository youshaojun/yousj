package top.yousj.core.annotation;

import org.springframework.context.annotation.Import;
import top.yousj.core.config.timecost.TimeCostBeanPostProcessor;
import top.yousj.core.config.timecost.TimeCostSpringbootRunner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Import({TimeCostBeanPostProcessor.class, TimeCostSpringbootRunner.class})
public @interface EnableTimeCostAnalyse {
}

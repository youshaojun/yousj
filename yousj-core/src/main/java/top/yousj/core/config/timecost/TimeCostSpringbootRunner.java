package top.yousj.core.config.timecost;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.util.Map;

/**
 * @author yousj
 * @since 2023-02-03
 */
@Slf4j
public class TimeCostSpringbootRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        for (Map.Entry<String, Long> entry : TimeCostBeanPostProcessor.costMap.entrySet()) {
            log.error("Spring Bean 初始化耗时统计[ bean: {} time: {} ms ]", entry.getKey(), entry.getValue());
        }
    }

}

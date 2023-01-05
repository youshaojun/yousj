package top.yousj.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
public class LogPointConfig {

	@Bean
	@ConditionalOnProperty(prefix = "top.yousj.web.log", name = "pointcut")
	@ConditionalOnMissingBean
	public LogPointHandler logPointHandler() {
		return logMap -> Optional.ofNullable(logMap).ifPresent(e -> log.info(e.toString()));
	}

}

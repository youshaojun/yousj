package top.yousj.log.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Optional;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "top.yousj.web.log", name = "pointcut")
public class LogPointAdvice {

	private final Environment environment;

	@Bean
	@ConditionalOnMissingBean
	public LogPointHandler logPointHandler() {
		return logMap -> Optional.ofNullable(logMap).ifPresent(e -> log.info(e.toString()));
	}

	@Bean
	public AspectJExpressionPointcutAdvisor webLogPointAdvisor(LogPointMethodInterceptor logPointMethodInterceptor) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setAdvice(logPointMethodInterceptor);
		advisor.setOrder(environment.getProperty("top.yousj.web.log.order", Integer.class, -1));
		advisor.setExpression(environment.getProperty("top.yousj.web.log.pointcut"));
		return advisor;
	}

}

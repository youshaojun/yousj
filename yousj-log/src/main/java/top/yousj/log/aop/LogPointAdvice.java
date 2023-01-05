package top.yousj.log.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
@RequiredArgsConstructor
public class LogPointAdvice {

	private final Environment environment;

	@Bean
	@ConditionalOnProperty(prefix = "top.yousj.web.log", name = "pointcut")
	public AspectJExpressionPointcutAdvisor webLogPointAdvisor(LogPointMethodInterceptor logPointMethodInterceptor) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setAdvice(logPointMethodInterceptor);
		advisor.setOrder(environment.getProperty("top.yousj.web.log.order", Integer.class, -1));
		advisor.setExpression(environment.getProperty("top.yousj.web.log.pointcut"));
		return advisor;
	}


}

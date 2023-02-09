package top.yousj.log.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yousj.log.constant.PropertyConstant;
import top.yousj.log.properties.LogProperties;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(LogProperties.class)
@ConditionalOnProperty(prefix = PropertyConstant.LOG, name = "aop.pointcut")
public class LogPointAdvice {

	private final ObjectMapper objectMapper;
	private final LogProperties logProperties;

	@Bean
	@ConditionalOnMissingBean
	public LogPointHandler defaultLogPointHandler() {
		return logMap -> log.info(objectMapper.writeValueAsString(logMap));
	}

	@Bean
	public AspectJExpressionPointcutAdvisor logPointAdvisor(LogPointMethodInterceptor logPointMethodInterceptor) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setAdvice(logPointMethodInterceptor);
		LogProperties.Aop aopProperties = logProperties.getAop();
		advisor.setOrder(aopProperties.getOrder());
		advisor.setExpression(aopProperties.getPointcut());
		return advisor;
	}

}

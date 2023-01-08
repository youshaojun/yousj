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
import top.yousj.core.constant.PropertyConstant;
import top.yousj.core.properties.TopYousjProperties;

import java.util.Objects;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(TopYousjProperties.class)
@ConditionalOnProperty(prefix = PropertyConstant.LOG, name = "aop.pointcut")
public class LogPointAdvice {

	private final ObjectMapper objectMapper;
	private final TopYousjProperties topYousjProperties;

	@Bean
	@ConditionalOnMissingBean
	public LogPointHandler logPointHandler() {
		return logMap -> {
			if (Objects.nonNull(logMap)) {
				log.info(objectMapper.writeValueAsString(logMap));
			}
		};
	}

	@Bean
	public AspectJExpressionPointcutAdvisor webLogPointAdvisor(LogPointMethodInterceptor logPointMethodInterceptor) {
		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setAdvice(logPointMethodInterceptor);
		TopYousjProperties.Log.Aop aopProperties = topYousjProperties.getLog().getAop();
		advisor.setOrder(aopProperties.getOrder());
		advisor.setExpression(aopProperties.getPointcut());
		return advisor;
	}

}

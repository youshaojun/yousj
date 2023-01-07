package top.yousj.log.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yousj.core.utils.SpringUtil;

import java.util.Objects;

/**
 * @author yousj
 * @since 2023-01-04
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "top.yousj.log.aop", name = "pointcut")
public class LogPointAdvice {

	private final ObjectMapper objectMapper;

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
		advisor.setOrder(Objects.requireNonNull(SpringUtil.getProperty("top.yousj.log.aop.order", Integer.class, -1)));
		advisor.setExpression(SpringUtil.getProperty("top.yousj.log.aop.pointcut"));
		return advisor;
	}

}

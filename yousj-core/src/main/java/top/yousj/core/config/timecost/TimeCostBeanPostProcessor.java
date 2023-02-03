package top.yousj.core.config.timecost;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import top.yousj.core.constant.PropertyConstant;
import top.yousj.core.properties.TimeCostProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimeCostBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

	public static Map<String, Long> costMap;
	private TimeCostProperties timeCostProperties;

	@Override
	public void setEnvironment(Environment environment) {
		timeCostProperties = Binder.get(environment).bind(PropertyConstant.TIME_COST, TimeCostProperties.class).orElse(new TimeCostProperties());
		costMap = new ConcurrentHashMap<>(timeCostProperties.getInitialCapacity());
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (!StringUtils.containsAny(beanName, timeCostProperties.getExcludes())) {
			costMap.put(beanName, System.currentTimeMillis());
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!costMap.containsKey(beanName)) {
			return bean;
		}
		Long start = costMap.get(beanName);
		long cost = System.currentTimeMillis() - start;
		if (cost > timeCostProperties.getThreshold()) {
			costMap.put(beanName, cost);
		} else {
			costMap.remove(beanName);
		}
		return bean;
	}

}
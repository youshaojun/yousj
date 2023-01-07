package top.yousj.redis.multi;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

/**
 * @author yousj
 * @since 2022-12-29
 */
public class MultiRedisBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		Binder.get(environment).bind("multi-redis", MultiRedisConfig.class).ifBound(multiRedisConfig -> registerBeanDefinition(multiRedisConfig, registry));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {

	}

	private void registerBeanDefinition(MultiRedisConfig multiRedisConfig, BeanDefinitionRegistry registry) {
		for (Map.Entry<String, MultiRedisStandaloneConfiguration> redisStandaloneConfiguration : multiRedisConfig.getConfigs().entrySet()) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RedisTemplate.class);
			GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
			definition.getConstructorArgumentValues().addGenericArgumentValue(redisStandaloneConfiguration.getValue());
			definition.setBeanClass(MultiRedisTemplateFactoryBean.class);
			definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
			registry.registerBeanDefinition(redisStandaloneConfiguration.getKey(), definition);
		}
	}

}

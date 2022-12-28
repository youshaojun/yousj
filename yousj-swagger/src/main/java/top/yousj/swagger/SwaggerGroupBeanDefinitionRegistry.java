package top.yousj.swagger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Component
public class SwaggerGroupBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Binder.get(environment).bind("multi-swagger-group", SwaggerGroups.class).ifBound(s ->
                s.getGroups().values().forEach(e -> {
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Docket.class);
                    GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
                    definition.getConstructorArgumentValues().addGenericArgumentValue(e);
                    definition.setBeanClass(SwaggerGroupFactoryBean.class);
                    definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_NAME);
                    registry.registerBeanDefinition(e.getGroupName(), definition);
                }));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}

package top.yousj.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringUtil implements BeanFactoryPostProcessor, ApplicationContextAware, EnvironmentAware {

	private static ConfigurableListableBeanFactory beanFactory;
	private static ApplicationContext applicationContext;
	private static Environment environment;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringUtil.beanFactory = beanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtil.applicationContext = applicationContext;
	}

	@Override
	public void setEnvironment(Environment environment) {
		SpringUtil.environment = environment;
	}


	/**
	 * 获取{@link ApplicationContext}
	 *
	 * @return {@link ApplicationContext}
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取{@link ListableBeanFactory}，可能为{@link ConfigurableListableBeanFactory} 或 {@link ApplicationContextAware}
	 *
	 * @return {@link ListableBeanFactory}
	 */
	public static ListableBeanFactory getBeanFactory() {
		return null == beanFactory ? applicationContext : beanFactory;
	}

	/**
	 * 通过name获取 Bean
	 *
	 * @param <T>  Bean类型
	 * @param name Bean名称
	 * @return Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) getBeanFactory().getBean(name);
	}

	/**
	 * 通过class获取Bean
	 *
	 * @param <T>   Bean类型
	 * @param clazz Bean类
	 * @return Bean对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		return getBeanFactory().getBean(clazz);
	}

	/**
	 * 通过name,以及Clazz返回指定的Bean
	 *
	 * @param <T>   bean类型
	 * @param name  Bean名称
	 * @param clazz bean类型
	 * @return Bean对象
	 */
	public static <T> T getBean(String name, Class<T> clazz) {
		return getBeanFactory().getBean(name, clazz);
	}

	/**
	 * 获取指定类型对应的所有Bean，包括子类
	 *
	 * @param <T>  Bean类型
	 * @param type 类、接口，null表示获取所有bean
	 * @return 类型对应的bean，key是bean注册的name，value是Bean
	 */
	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	/**
	 * 获取指定类型对应的Bean名称，包括子类
	 *
	 * @param type 类、接口，null表示获取所有bean名称
	 * @return bean名称
	 */
	public static String[] getBeanNamesForType(Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	/**
	 * 获取配置文件配置项的值
	 *
	 * @param key 配置项key
	 * @return 属性值
	 */
	public static String getProperty(String key) {
		return environment.getProperty(key);
	}

	public static <T> T getProperty(String key, Class<T> clazz, T defaultValue) {
		return environment.getProperty(key, clazz, defaultValue);
	}

	/**
	 * 获取应用程序名称
	 *
	 * @return 应用程序名称
	 */
	public static String getApplicationName() {
		return getProperty("spring.application.name");
	}

	/**
	 * 获取当前的环境配置，无配置返回null
	 *
	 * @return 当前的环境配置
	 */
	public static String[] getProfiles() {
		return environment.getActiveProfiles();
	}

	/**
	 * 获取当前激活环境
	 *
	 * @return 当前激活环境
	 */
	public static String getActiveProfile() {
		return environment.getProperty("spring.profiles.active");
	}

}





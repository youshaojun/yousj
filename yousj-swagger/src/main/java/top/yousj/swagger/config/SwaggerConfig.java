package top.yousj.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Slf4j
@EnableKnife4j
@Configuration
@Profile({"dev", "test"})
public class SwaggerConfig {

    static {
        System.setProperty("spring.mvc.pathmatch.matching-strategy", "ant_path_matcher");
    }

    public SwaggerConfig(List<Docket> dockets) {
        dockets.forEach(e -> log.debug(" load swagger group [" + e.getGroupName() + "]"));
    }

    @Bean
    @SuppressWarnings("all")
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    getHandlerMappings(bean).removeIf(e -> Objects.isNull(e.getPatternParser()));
                }
                return bean;
            }

            @SneakyThrows
            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                field.setAccessible(true);
                return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
            }
        };
    }

}

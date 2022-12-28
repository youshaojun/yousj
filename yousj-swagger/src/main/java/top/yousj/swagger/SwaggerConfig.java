package top.yousj.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Import(BeanValidatorPluginsConfiguration.class)
@EnableKnife4j
@EnableSwagger2
@Configuration
@Profile({"dev", "test"})
@Slf4j
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        SwaggerGroups.SwaggerGroup swaggerGroup = new SwaggerGroups.SwaggerGroup();
        swaggerGroup.setGroupName("a.全部");
        return SwaggerBuilder.of(swaggerGroup);
    }

}

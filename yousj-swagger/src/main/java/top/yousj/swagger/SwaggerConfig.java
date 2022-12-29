package top.yousj.swagger;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Slf4j
@EnableKnife4j
@Configuration
@Profile({"dev", "test"})
public class SwaggerConfig {

    public SwaggerConfig(List<Docket> dockets) {
        dockets.forEach(e -> log.debug(" load swagger group [" + e.getGroupName() + "]"));
    }

    @Configuration
    public static class DefaultDocket {

        @Bean
        public Docket docket() {
            SwaggerGroups.SwaggerGroup swaggerGroup = new SwaggerGroups.SwaggerGroup();
            swaggerGroup.setGroupName("a.全部");
            return SwaggerDocket.of(swaggerGroup);
        }
    }

}

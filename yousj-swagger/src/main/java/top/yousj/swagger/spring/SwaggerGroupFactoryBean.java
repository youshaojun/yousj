package top.yousj.swagger.spring;

import org.springframework.beans.factory.FactoryBean;
import springfox.documentation.spring.web.plugins.Docket;
import top.yousj.swagger.config.SwaggerDocket;
import top.yousj.swagger.entity.SwaggerGroups;

/**
 * @author yousj
 * @since 2022-12-28
 */
public class SwaggerGroupFactoryBean implements FactoryBean<Docket> {

    private final SwaggerGroups.SwaggerGroup swaggerGroup;

    public SwaggerGroupFactoryBean(SwaggerGroups.SwaggerGroup swaggerGroup) {
        this.swaggerGroup = swaggerGroup;
    }

    @Override
    public Docket getObject() {
        return SwaggerDocket.of(swaggerGroup);
    }

    @Override
    public Class<?> getObjectType() {
        return Docket.class;
    }

}

package top.yousj.swagger;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Data
@Component
@ConfigurationProperties(prefix = "multi-swagger-group")
class SwaggerGroups {

    private Map<String, SwaggerGroup> groups;

    @Data
	@Accessors(chain = true)
    static class SwaggerGroup {
        private String groupName;
        private String basePackage;
        private String title = "v1.0";
        private String description = "v1.0";
        private String version = "1.0.0";
        private String url = "https://www.yousj.com";
        private String contactName = "yousj";
        private String contactEmail = "youshaojunde@163.com";
    }
}

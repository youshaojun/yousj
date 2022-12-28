package top.yousj.swagger;

import lombok.Data;

import java.util.Map;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Data
class SwaggerGroups {

    private Map<String, SwaggerGroup> groups;

    @Data
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

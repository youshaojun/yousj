package top.yousj.freemarker.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.freemarker.constant.PropertyConstant;

/**
 * @author yousj
 * @since 2023-02-10
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.FREEMARKER)
public class FreeMarkerProperties {

	private boolean cache;

	private String basePackagePath = "/template";

	private String templateSuffix = ".ftl";

}

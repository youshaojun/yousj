package top.yousj.freemark.properties;

import lombok.Data;

/**
 * @author yousj
 * @since 2023-02-10
 */
@Data
public class FreeMarkerProperties {

	private boolean cache = true;

	private String basePackagePath = "/template";

	private String templateSuffix = ".ftl";

}

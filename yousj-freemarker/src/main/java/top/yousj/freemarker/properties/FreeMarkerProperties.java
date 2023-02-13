package top.yousj.freemarker.properties;

import lombok.Data;

/**
 * @author yousj
 * @since 2023-02-10
 */
@Data
public class FreeMarkerProperties {

	public static final FreeMarkerProperties INSTANCE = new FreeMarkerProperties();

	private String basePackagePath = "/template";

	private String templateSuffix = ".ftl";

}

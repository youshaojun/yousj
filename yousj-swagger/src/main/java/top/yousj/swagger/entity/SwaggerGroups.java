package top.yousj.swagger.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.swagger.constant.PropertyConstant;

import java.util.Map;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.SWAGGER)
public class SwaggerGroups {

	private Map<String, SwaggerGroup> groups;

	@Data
	@Accessors(chain = true)
	public static class SwaggerGroup {

		/**
		 * 分组名称
		 */
		private String groupName;

		/**
		 * 扫描包
		 */
		private String basePackage;

		/**
		 * 标题
		 */
		private String title = "v1.0";

		/**
		 * 描述
		 */
		private String description = "v1.0";

		/**
		 * 版本号
		 */
		private String version = "1.0.0";

		/**
		 * url
		 */
		private String url = "https://www.yousj.com";

		/**
		 * 联系人
		 */
		private String contactName = "yousj";

		/**
		 * 邮箱
		 */
		private String contactEmail = "youshaojunde@163.com";

	}
}

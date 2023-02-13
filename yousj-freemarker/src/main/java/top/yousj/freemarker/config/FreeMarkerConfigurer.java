package top.yousj.freemarker.config;

import freemarker.core.XMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import top.yousj.commons.constant.StrPool;
import top.yousj.freemarker.properties.FreeMarkerProperties;
import top.yousj.freemarker.utils.FreeMarkUtil;

/**
 * @author yousj
 * @since 2023-02-10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreeMarkerConfigurer {

	@SneakyThrows
	public static Template getTemplate(String templateName, FreeMarkerProperties freeMarkerProperties) {
		Configuration config = new Configuration(Configuration.VERSION_2_3_28);
		config.setClassForTemplateLoading(FreeMarkUtil.class, freeMarkerProperties.getBasePackagePath());
		config.setDefaultEncoding(StrPool.CHARSET_NAME);
		config.setOutputFormat(XMLOutputFormat.INSTANCE);
		config.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		return config.getTemplate(templateName + freeMarkerProperties.getTemplateSuffix());
	}

}

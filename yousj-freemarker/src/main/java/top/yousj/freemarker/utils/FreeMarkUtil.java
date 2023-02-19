package top.yousj.freemarker.utils;

import freemarker.template.Template;
import lombok.SneakyThrows;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.commons.utils.FileUtil;
import top.yousj.freemarker.config.FreeMarkerConfigurer;
import top.yousj.freemarker.properties.FreeMarkerProperties;

import java.io.*;
import java.util.Map;

/**
 * @author yousj
 * @since 2023-02-06
 */
public class FreeMarkUtil {

	public static String asString(Map<String, Object> data, String templateName) {
		return asString(data, templateName, FreeMarkerProperties.INSTANCE);
	}

	@SneakyThrows
	public static String asString(Map<String, Object> data, String templateName, FreeMarkerProperties freeMarkerProperties) {
		Template template = FreeMarkerConfigurer.getTemplate(templateName, freeMarkerProperties);
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
	}

	public static File process(Map<String, Object> data, String templateName, FileTypeEnum fileTypeEnum) {
		return process(data, FreeMarkerProperties.INSTANCE, templateName, fileTypeEnum);
	}

	public static File process(Map<String, Object> data, FreeMarkerProperties freeMarkerProperties, String templateName, FileTypeEnum fileTypeEnum) {
		Template template = FreeMarkerConfigurer.getTemplate(templateName, freeMarkerProperties);
		return process(template, data, fileTypeEnum);
	}

	@SneakyThrows
	public static File process(Template template, Map<String, Object> data, FileTypeEnum fileTypeEnum) {
		File file = FileUtil.create(fileTypeEnum);
		FileOutputStream fos = new FileOutputStream(file);
		try (Writer out = new BufferedWriter(new OutputStreamWriter(fos))) {
			template.process(data, out);
		}
		return file;
	}

}

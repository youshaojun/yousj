package top.yousj.freemarker.utils;

import freemarker.template.Template;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.commons.utils.FileUtil;
import top.yousj.freemarker.config.FreeMarkerConfigurer;
import top.yousj.freemarker.properties.FreeMarkerProperties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author yousj
 * @since 2023-02-06
 */
@Component
public class FreeMarkUtil {

	private static FreeMarkerProperties freeMarkerProperties;

	@Autowired
	public FreeMarkUtil(FreeMarkerProperties freeMarkerProperties) {
		FreeMarkUtil.freeMarkerProperties = freeMarkerProperties;
	}

	@SneakyThrows
	public static String asString(Map<String, Object> data, String templateName) {
		File file = process(data, templateName, FileTypeEnum.HTML);
		Path path = Paths.get(file.getAbsolutePath());
		String content = Files.readString(path);
		Files.delete(path);
		return content;
	}

	public static File process(Map<String, Object> data, String templateName, FileTypeEnum fileTypeEnum) {
		Template template = FreeMarkerConfigurer.getTemplate(templateName, ObjectUtils.defaultIfNull(freeMarkerProperties, new FreeMarkerProperties()));
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

package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * 获取resources资源文件
 *
 * @author yousj
 * @since 2023-01-05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourcesUtil {

	@SneakyThrows
	public static File getFile(String path) {
		Objects.requireNonNull(path);
		InputStream input = ResourcesUtil.class.getClassLoader().getResourceAsStream(path);
		if (Objects.isNull(input)) {
			return null;
		}
		String[] split = path.split("\\.");
		File file = new File(ExportUtil.createPath(split[split.length - 1]));
		Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return file;
	}

}

package top.yousj.commons.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;
import top.yousj.commons.constant.StrPool;
import top.yousj.commons.enums.FileTypeEnum;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author yousj
 * @since 2023-02-10
 */
public class FileUtil {

	public static File create(FileTypeEnum fileTypeEnum) {
		return mkdirs(new File(createPath(fileTypeEnum)));
	}

	public static File create(String suffix) {
		return mkdirs(new File(createPath(suffix)));
	}

	public static String createPath(FileTypeEnum fileTypeEnum) {
		return createPath(StrPool.TMP, fileTypeEnum);
	}

	public static String createPath(String suffix) {
		return createPath(StrPool.TMP, suffix);
	}

	public static String createPath(String dir, FileTypeEnum fileTypeEnum) {
		Objects.requireNonNull(fileTypeEnum);
		return createPath(dir, fileTypeEnum.name().toLowerCase());
	}

	public static String createPath(String dir, String suffix) {
		return dir + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + UUID.randomUUID().toString() + "." + suffix;
	}

	@SneakyThrows
	public static File mkdirs(File file) {
		if (file.exists()) {
			return file;
		}
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		file.createNewFile();
		return file;
	}

	@SneakyThrows
	public static File getResources(String path) {
		Objects.requireNonNull(path);
		InputStream input = FileUtil.class.getClassLoader().getResourceAsStream(path);
		if (Objects.isNull(input)) {
			return null;
		}
		String[] split = path.split("\\.");
		File file = new File(createPath(split[split.length - 1]));
		Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		return file;
	}

}

package top.yousj.core.utils;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;
import top.yousj.core.constant.FileTypeEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * 导出工具
 *
 * @author yousj
 * @since 2023-01-05
 */
public class ExportUtil {

	public static void download(String path, String fileName, FileTypeEnum fileTypeEnum, HttpServletResponse res) {
		download(new File(path), fileName, fileTypeEnum, res);
	}

	@SneakyThrows
	public static void download(File file, String fileName, FileTypeEnum fileTypeEnum, HttpServletResponse res) {
		Objects.requireNonNull(fileTypeEnum);
		download(Files.readAllBytes(file.toPath()), fileName, fileTypeEnum.name().toLowerCase(), res);
	}

	public static void download(byte[] bytes, String fileName, HttpServletResponse res) {
		download(bytes, fileName, null, res);
	}

	@SneakyThrows
	public static void download(byte[] bytes, String fileName, String suffix, HttpServletResponse res) {
		try (ServletOutputStream stream = res.getOutputStream()) {
			res.setCharacterEncoding("utf-8");
			res.setContentType("application/octet-stream");
			res.addHeader("Content-Length", "" + bytes.length);
			res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8") + (Objects.nonNull(suffix) ? "." + suffix : ""));
			stream.write(bytes);
			stream.flush();
		}
	}

	public static String createPath(FileTypeEnum fileTypeEnum) {
		return createPath("/tmp", fileTypeEnum);
	}

	public static String createPath(String suffix) {
		return createPath("/tmp", suffix);
	}

	public static String createPath(String dir, FileTypeEnum fileTypeEnum) {
		Objects.requireNonNull(fileTypeEnum);
		return createPath(dir, fileTypeEnum.name().toLowerCase());
	}

	public static String createPath(String dir, String suffix) {
		return dir + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + UUID.randomUUID().toString() + "." + suffix;
	}

}

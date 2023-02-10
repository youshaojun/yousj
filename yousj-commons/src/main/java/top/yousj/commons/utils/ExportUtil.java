package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import top.yousj.commons.enums.FileTypeEnum;
import top.yousj.commons.constant.StrPool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Objects;

/**
 * 导出工具
 *
 * @author yousj
 * @since 2023-01-05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
			res.setCharacterEncoding(StrPool.CHARSET_NAME);
			res.setContentType("application/octet-stream");
			res.addHeader("Content-Length", "" + bytes.length);
			res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StrPool.CHARSET_NAME) + (Objects.nonNull(suffix) ? "." + suffix : StrPool.EMPTY));
			stream.write(bytes);
			stream.flush();
		}
	}

}

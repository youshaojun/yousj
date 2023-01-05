package top.yousj.reload.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.yousj.core.entity.R;
import top.yousj.core.utils.ExportUtil;
import top.yousj.reload.service.JvmAttachClassReloadService;
import top.yousj.reload.service.MapperReloadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reload")
public class RetransformController {

	private final MapperReloadService mapperReloadService;

	@RequestMapping("/updateClass")
	public R<String> updateClass(@RequestParam("classFile") MultipartFile classFile, String className) {
		// class 热加载
		if (classFile == null || classFile.isEmpty()) {
			return R.failure("文件不能为空");
		}
		if (StringUtils.isBlank(className)) {
			return R.failure("类名不能为空");
		}
		className = className.trim();
		try {
			String originalFilename = classFile.getOriginalFilename();
			Objects.requireNonNull(originalFilename);
			if (!originalFilename.endsWith(".class")) {
				return R.failure("仅支持.class文件");
			}
			String[] filename = originalFilename.split("\\.");
			String substring = className.substring(className.lastIndexOf(".") + 1);
			if (!substring.equals(filename[0])) {
				return R.failure("请确认类名是否正确");
			}
			File file = uploadFile(classFile.getBytes(), filename[0]);
			Objects.requireNonNull(file);
			JvmAttachClassReloadService.getInstance().updateClass(className, file.getAbsolutePath());
			file.deleteOnExit();
		} catch (Exception e) {
			log.error("" + e);
			return R.failure("无法解析文件");
		}
		return R.ok("更新成功");
	}

	@SneakyThrows
	@RequestMapping("/updateMapperXml")
	public R<String> updateMapperXml(@RequestParam("mapperXmlFile") MultipartFile mapperXmlFile) {
		String originalFilename = mapperXmlFile.getOriginalFilename();
		String suffix = org.apache.commons.lang3.StringUtils.substringAfterLast(originalFilename, ".");
		File file = ExportUtil.newFile(suffix);
		Files.copy(mapperXmlFile.getInputStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		mapperReloadService.reloadXml(file);
		return R.ok();
	}

	private static File uploadFile(byte[] file, String fileName) throws IOException {
		File targetFile = File.createTempFile(fileName, ".class", new File(System.getProperty("java.io.tmpdir")));
		try (FileOutputStream out = new FileOutputStream(targetFile.getAbsolutePath())) {
			out.write(file);
			out.flush();
			return targetFile;
		}
	}

}

package top.yousj.reload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.yousj.core.entity.R;
import top.yousj.reload.service.JvmAttachClassReloadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Slf4j
@RestController
@RequestMapping("/reload")
public class RetransformController {

	@RequestMapping("/updateClass")
	public R<String> updateClass(@RequestParam("classFile") MultipartFile classFile, String className) {
		// class 热加载
		if (classFile == null || classFile.isEmpty()) {
			return R.failure("文件不能为空");
		}
		if (!StringUtils.hasText(className)) {
			return R.failure("类名不能为空");
		}
		className = className.trim();
		File file;
		try {
			String originalFilename = classFile.getOriginalFilename();
			if (!originalFilename.endsWith(".class")) {
				return R.failure("仅支持.class文件");
			}
			String[] filename = originalFilename.split("\\.");
			String substring = className.substring(className.lastIndexOf(".") + 1);
			if (!substring.equals(filename[0])) {
				return R.failure("请确认类名是否正确");
			}
			file = uploadFile(classFile.getBytes(), filename[0]);
		} catch (IOException e) {
			return R.failure("无法解析文件");
		}
		JvmAttachClassReloadService.getInstance().updateClass(className, file.getAbsolutePath());
		file.deleteOnExit();
		return R.ok("更新成功");
	}

	@RequestMapping("/updateMapperXml")
	public R<String> updateMapperXml(@RequestParam("mapperXmlFile") MultipartFile mapperXmlFile) {
		// TODO Mybatis XML 热加载
		return R.ok();
	}

	private static File uploadFile(byte[] file, String fileName) throws IOException {
		FileOutputStream out = null;
		try {
			File targetFile = File.createTempFile(fileName, ".class", new File(System.getProperty("java.io.tmpdir")));
			out = new FileOutputStream(targetFile.getAbsolutePath());
			out.write(file);
			out.flush();
			return targetFile;
		} catch (Exception e) {
			log.error("" + e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		return null;
	}

}

package top.yousj.reload.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.yousj.core.entity.R;
import top.yousj.core.utils.ExportUtil;
import top.yousj.reload.service.MapperReloadService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * @author yousj
 * @since 2023-01-05
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reload")
@Profile({"dev", "test"})
public class ReloadMapperController {

	private final MapperReloadService mapperReloadService;

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

}

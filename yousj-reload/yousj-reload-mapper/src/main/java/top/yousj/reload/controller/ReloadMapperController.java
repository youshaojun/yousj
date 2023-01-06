package top.yousj.reload.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.yousj.core.entity.R;
import top.yousj.core.utils.ExportUtil;
import top.yousj.core.utils.SpringUtil;
import top.yousj.reload.service.MapperReloadService;

import javax.annotation.PostConstruct;
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

	@PostConstruct
	public void check() {
		if (!StringUtils.equalsAny(SpringUtil.getActiveProfile(), "dev", "test")) {
			log.error("热更新功能只支持在开发和测试阶段使用!");
			log.error("请将reload相关maven依赖置于正确的profile内!");
			System.exit(-1);
		}
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

}

package top.yousj.uaa.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yousj.security.config.CustomConfig;
import top.yousj.uaa.entity.po.UaaAuthUrlConfig;
import top.yousj.uaa.enums.UrlTypeEnum;
import top.yousj.uaa.service.IUaaAuthUrlConfigService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static top.yousj.security.config.CustomConfig.IGNORE_URLS;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ReloadCustomConfigServiceImpl {

	private final IUaaAuthUrlConfigService uaaAuthUrlConfigService;

	static {
		IGNORE_URLS.add("/uaa/login");
		IGNORE_URLS.add("/uaa/logout");
	}

	@Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
	public void reload() {
		List<UaaAuthUrlConfig> list = uaaAuthUrlConfigService.list(
			Wrappers.<UaaAuthUrlConfig>lambdaQuery()
				.select(UaaAuthUrlConfig::getAppName, UaaAuthUrlConfig::getAuthUrl, UaaAuthUrlConfig::getUrlType, UaaAuthUrlConfig::getIsDeleted)
		);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		Map<String, List<UaaAuthUrlConfig>> group = list.stream().collect(Collectors.groupingBy(UaaAuthUrlConfig::getAppName));

		for (Map.Entry<String, List<UaaAuthUrlConfig>> entry : group.entrySet()) {
			String appName = entry.getKey();
			List<UaaAuthUrlConfig> urls = entry.getValue();
			Stream<UaaAuthUrlConfig> ignoreConfig = urls.stream().filter(e -> Objects.equals(e.getUrlType(), UrlTypeEnum.IGNORE.getCode()));
			Stream<UaaAuthUrlConfig> authConfig = urls.stream().filter(e -> Objects.equals(e.getUrlType(), UrlTypeEnum.AUTH.getCode()));
			Stream<UaaAuthUrlConfig> allConfig = urls.stream().filter(e -> Objects.equals(e.getUrlType(), UrlTypeEnum.ALL.getCode()));

			reload(appName, ignoreConfig, CustomConfig.Multiple.SELF_IGNORE_URLS);
			reload(appName, authConfig, CustomConfig.Multiple.AUTH_PERMIT_URLS);
			reload(appName, allConfig, CustomConfig.Multiple.ALL_URLS);

		}

	}

	private void reload(String appName, Stream<UaaAuthUrlConfig> configs, Map<String, Set<String>> multipleConfig) {
		Set<String> urls = multipleConfig.get(appName);
		if (CollectionUtils.isEmpty(urls)) {
			multipleConfig.put(appName, configs.map(UaaAuthUrlConfig::getAuthUrl).collect(Collectors.toSet()));
			return;
		}
		configs.forEach(e -> {
			if (Objects.equals(1, e.getIsDeleted())) {
				urls.remove(e.getAuthUrl());
				return;
			}
			urls.add(e.getAuthUrl());
		});
	}

}

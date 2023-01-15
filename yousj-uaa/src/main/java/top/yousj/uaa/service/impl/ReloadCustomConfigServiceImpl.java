package top.yousj.uaa.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yousj.security.config.CustomConfig;
import top.yousj.uaa.entity.UaaAuthUrlConfig;
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
				.select(UaaAuthUrlConfig::getAppName, UaaAuthUrlConfig::getAuthUrl, UaaAuthUrlConfig::getIsDeleted)
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
			Set<String> ignore = CustomConfig.Multiple.SELF_IGNORE_URLS.get(appName);
			Set<String> auth = CustomConfig.Multiple.AUTH_PERMIT_URLS.get(appName);
			Set<String> all = CustomConfig.Multiple.ALL_URLS.get(appName);

			reload(appName, ignoreConfig, ignore);
			reload(appName, authConfig, auth);
			reload(appName, allConfig, all);

		}

	}

	private void reload(String appName, Stream<UaaAuthUrlConfig> configs, Set<String> urls) {
		if (CollectionUtils.isEmpty(urls)) {
			CustomConfig.Multiple.AUTH_PERMIT_URLS.put(appName, configs.map(UaaAuthUrlConfig::getAuthUrl).collect(Collectors.toSet()));
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

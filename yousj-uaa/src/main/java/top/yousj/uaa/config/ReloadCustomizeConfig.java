package top.yousj.uaa.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import top.yousj.commons.entity.R;
import top.yousj.security.config.CustomizeConfig;
import top.yousj.uaa.entity.po.UaaAuthUrlConfig;
import top.yousj.uaa.entity.po.UaaUserDataSource;
import top.yousj.uaa.enums.UrlTypeEnum;
import top.yousj.uaa.service.IUaaAuthUrlConfigService;
import top.yousj.uaa.service.IUaaUserDataSourceService;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static top.yousj.security.config.CustomizeConfig.IGNORE_URLS;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ReloadCustomizeConfig {

	private final RestTemplate restTemplate;
	private final IUaaAuthUrlConfigService uaaAuthUrlConfigService;
	private final IUaaUserDataSourceService uaaUserDataSourceService;

	static {
		IGNORE_URLS.add("/uaa/login");
		IGNORE_URLS.add("/uaa/logout");
		IGNORE_URLS.add("/uaa/getMappingUrls");
	}

	@Scheduled(fixedDelay = 10, timeUnit = TimeUnit.MINUTES)
	@SuppressWarnings("unchecked")
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

			reload(appName, ignoreConfig, CustomizeConfig.Uaa.SELF_IGNORE_URLS);
			reload(appName, authConfig, CustomizeConfig.Uaa.AUTH_PERMIT_URLS);
		}

		List<UaaUserDataSource> uaaUserDataSources = uaaUserDataSourceService.list(Wrappers.<UaaUserDataSource>lambdaQuery()
			.select(UaaUserDataSource::getId, UaaUserDataSource::getAppName, UaaUserDataSource::getQueryAllPathUrl));

		uaaUserDataSources.forEach(e -> {
			try {
				CustomizeConfig.Uaa.ALL_URLS.put(e.getAppName(), (Set<String>) Objects.requireNonNull(restTemplate.getForObject(e.getQueryAllPathUrl(), R.class)).getData());
			} catch (Exception ignored) {
			}
		});

	}

	private void reload(String appName, Stream<UaaAuthUrlConfig> configs, Map<String, Set<String>> uaaConfig) {
		Set<String> urls = uaaConfig.get(appName);
		if (CollectionUtils.isEmpty(urls)) {
			uaaConfig.put(appName, configs.map(UaaAuthUrlConfig::getAuthUrl).collect(Collectors.toSet()));
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

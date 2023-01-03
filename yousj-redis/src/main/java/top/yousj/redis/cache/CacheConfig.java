package top.yousj.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.yousj.redis.RedisTemplateFactory;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Slf4j
@EnableCaching
@ConditionalOnClass(RedisOperations.class)
public class CacheConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("#{'${cache.config.scanPackages}'.split(',')}")
	private List<String> scanPackages;

	@Bean
	@ConditionalOnMissingBean
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		return new RedisCacheManager(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory), defaultRedisCacheConfiguration(), getCacheConfigurations());
	}

	private Map<String, RedisCacheConfiguration> getCacheConfigurations() {
		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
		cacheConfigurations.put(CacheConstant.LONG, generateRedisCacheConfiguration(Duration.ofDays(1L), simple(CacheConstant.LONG)));
		cacheConfigurations.put(CacheConstant.SHORT, generateRedisCacheConfiguration(Duration.ofMinutes(10L), simple(CacheConstant.SHORT)));
		Set<Method> methods = new HashSet<>();
		for (String scanPackage : scanPackages) {
			methods.addAll(new Reflections(new ConfigurationBuilder()
				.addUrls(ClasspathHelper.forPackage(scanPackage))
				.addScanners(new MethodAnnotationsScanner())).getMethodsAnnotatedWith(Cacheable.class));
		}
		for (Method method : methods) {
			Cacheable cacheable = method.getAnnotation(Cacheable.class);
			if (Objects.isNull(cacheable) || cacheable.cacheNames().length == 0) {
				continue;
			}
			// 通过cacheNames支持自定义过期策略(秒级) ==> xxx#ttl
			String cacheName = cacheable.cacheNames()[0];
			String[] split = cacheName.split("#");
			if (split.length == 2) {
				cacheConfigurations.put(cacheName, generateRedisCacheConfiguration(Duration.ofSeconds(Long.valueOf(split[1])), simple(CacheConstant.CUSTOM)));
			}
		}
		return cacheConfigurations;
	}

	private RedisCacheConfiguration defaultRedisCacheConfiguration() {
		return generateRedisCacheConfiguration(null, null);
	}

	private RedisCacheConfiguration generateRedisCacheConfiguration(Duration entryTtl, String prefixKey) {
		String prefixKeyWith = applicationName + ":spring:cache:";
		return RedisCacheConfiguration.defaultCacheConfig()
			.entryTtl(entryTtl == null ? Duration.ofHours(1L) : entryTtl)
			.disableCachingNullValues()
			.computePrefixWith((cacheName) -> prefixKey == null ? prefixKeyWith + (CacheConstant.COMMON) : prefixKeyWith + prefixKey)
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisTemplateFactory.getValueSerializer()));
	}

	private String simple(String key) {
		return key + ":";
	}

}

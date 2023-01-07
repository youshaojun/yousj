package top.yousj.redis.cache;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.yousj.core.utils.ParamAssertUtil;
import top.yousj.redis.RedisTemplateFactory;
import top.yousj.redis.utils.RedisUtil;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

import static top.yousj.redis.utils.RedisUtil.simple;

/**
 * 通过key支持自定义过期策略(秒级)
 * 添加缓存 @Cacheable(cacheNames = "searchCacheGroup", key = "'searchCache#3600#_' + " + "#id")
 * 删除缓存 @CacheEvict(cacheNames = "searchCacheGroup", allEntries = true)
 * 按cacheNames进行分组缓存, 同一个分组内过期时间相同
 * 按cacheNames进行分组删除缓存, 同一分组内全部删除
 *
 * @author yousj
 * @since 2022-12-29
 */
@Slf4j
@EnableCaching
@Configuration
@AutoConfigureAfter(RedisConnectionFactory.class)
@ConditionalOnClass(RedisOperations.class)
@ConditionalOnProperty(prefix = "top.yousj.redis.config", name = "scan-packages")
public class CacheConfig {

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("#{'${top.yousj.redis.config.scan-packages}'.split(',')}")
	private List<String> scanPackages;

	@Bean
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
			if (Objects.isNull(cacheable)) {
				continue;
			}
			String key = cacheable.key();
			ParamAssertUtil.notBlank(key, "key is not be blank. ");
			String[] split = key.replace("'", "").split("#");
			if (split.length < 2) {
				continue;
			}
			Duration ttl = Duration.ofSeconds(Long.valueOf(split[1]));
			Arrays.stream(cacheable.cacheNames()).forEach(group -> cacheConfigurations.put(group, generateRedisCacheConfiguration(ttl, simple(CacheConstant.CUSTOM))));
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
			.computePrefixWith((cacheName) -> prefixKey == null ? prefixKeyWith + RedisUtil.simple(CacheConstant.COMMON) : prefixKeyWith + prefixKey)
			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
			.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisTemplateFactory.getValueSerializer()));
	}

}

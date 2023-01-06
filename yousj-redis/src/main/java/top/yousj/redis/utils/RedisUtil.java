package top.yousj.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

	private static RedisTemplate<String, Object> redisTemplate;
	private static String appName;

	@Autowired
	public RedisUtil(RedisTemplate<String, Object> redisTemplate, Environment environment) {
		RedisUtil.redisTemplate = redisTemplate;
		RedisUtil.appName = environment.getProperty("spring.application.name") + ":";
	}

	public static <T> T get(String key) {
		return (T) redisTemplate.boundValueOps(withKey(key)).get();
	}

	public static Long get(String key, TimeUnit timeUnit) {
		return redisTemplate.getExpire(withKey(key), timeUnit);
	}

	public static Long getExpire(String key) {
		return redisTemplate.getExpire(withKey(key), TimeUnit.MILLISECONDS);
	}

	public static Boolean exist(String key) {
		Long expire = getExpire(key);
		return Objects.nonNull(expire) && expire > 0;
	}

	public static void put(String key, Object v) {
		redisTemplate.boundValueOps(withKey(key)).set(v);
	}

	public static void put(String key, Object v, long expire, TimeUnit timeUnit) {
		BoundValueOperations<String, Object> oper = redisTemplate.boundValueOps(withKey(key));
		oper.set(v);
		if (expire > 0 && timeUnit != null) {
			oper.expire(expire, timeUnit);
		}
	}

	public static void putIfAbsent(String key, Object v, long expire, TimeUnit timeUnit) {
		BoundValueOperations<String, Object> oper = redisTemplate.boundValueOps(withKey(key));
		Boolean ifAbsent = oper.setIfAbsent(v);
		if (Objects.equals(ifAbsent, true) && expire > 0 && timeUnit != null) {
			oper.expire(expire, timeUnit);
		}
	}

	public static Boolean del(String key) {
		return redisTemplate.delete(withKey(key));
	}

	public static void delHash(String key, String hashKey) {
		redisTemplate.opsForHash().delete(withKey(key), hashKey);
	}

	public static String withKey(String key) {
		return appName + key;
	}

}

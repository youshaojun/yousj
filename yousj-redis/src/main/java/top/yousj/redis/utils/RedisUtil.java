package top.yousj.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

	private static RedisTemplate<String, Object> redisTemplate;

	@Autowired
	public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
		RedisUtil.redisTemplate = redisTemplate;
	}

	public static <T> T get(String key) {
		return (T) redisTemplate.boundValueOps(key).get();
	}

	public static Long get(String key, TimeUnit timeUnit) {
		return redisTemplate.getExpire(key, timeUnit);
	}

	public static Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
	}

	public static Boolean exist(String key) {
		Long expire = getExpire(key);
		return Objects.nonNull(expire) && expire > 0;
	}

	public static void put(String key, Object v) {
		redisTemplate.boundValueOps(key).set(v);
	}

	public static void put(String key, Object v, long expire, TimeUnit timeUnit) {
		BoundValueOperations<String, Object> oper = redisTemplate.boundValueOps(key);
		oper.set(v);
		if (expire > 0 && timeUnit != null) {
			oper.expire(expire, timeUnit);
		}
	}

	public static void putIfAbsent(String key, Object v, long expire, TimeUnit timeUnit) {
		BoundValueOperations<String, Object> oper = redisTemplate.boundValueOps(key);
		Boolean ifAbsent = oper.setIfAbsent(v);
		if (Objects.equals(ifAbsent, true) && expire > 0 && timeUnit != null) {
			oper.expire(expire, timeUnit);
		}
	}

	public static Boolean del(String key) {
		return redisTemplate.delete(key);
	}

	public static void del(Set<String> keys) {
		redisTemplate.delete(keys);
	}

	public static void delHash(String key, String hashKey) {
		redisTemplate.opsForHash().delete(key, hashKey);
	}

}

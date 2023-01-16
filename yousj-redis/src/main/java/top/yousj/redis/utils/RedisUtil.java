package top.yousj.redis.utils;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import top.yousj.core.utils.NumberUtil;
import top.yousj.core.utils.SpringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class RedisUtil {

	private static RedisTemplate<String, Object> redisTemplate;
	private static StringRedisTemplate stringRedisTemplate;

	@Autowired
	public RedisUtil(RedisTemplate<String, Object> redisTemplate, StringRedisTemplate stringRedisTemplate) {
		RedisUtil.redisTemplate = redisTemplate;
		RedisUtil.stringRedisTemplate = stringRedisTemplate;
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

	public static <T> T put(String key, Supplier<T> valueSupplier, Long ttl) {
		return put(key, valueSupplier, ttl, TimeUnit.MILLISECONDS);
	}

	public static <T> T put(String key, Supplier<T> valueSupplier, Long ttl, TimeUnit timeUnit) {
		if (NumberUtil.gt0(ttl)) {
			T t = get(key);
			if (Objects.nonNull(t)) {
				return t;
			}
			t = valueSupplier.get();
			put(key, t, ttl, timeUnit);
			return t;
		}
		return valueSupplier.get();
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

	public static String simple(Object... keys) {
		return Stream.of(keys).filter(Objects::nonNull).map(String::valueOf).filter(StringUtils::isNotBlank).collect(Collectors.joining(":")) + ":";
	}

	public static String withKey(String key) {
		return simple(SpringUtil.getApplicationName()) + key;
	}

	public static String getSearchRecordKey(Integer uid) {
		return withKey(simple("search", "record", uid));
	}

	public void setSearchRecord(Integer uid, String searchStr) {
		setSearchRecord(uid, searchStr, 10);
	}

	/**
	 * 保存用户搜索历史
	 *
	 * @param uid       用户id
	 * @param searchStr 搜索信息
	 */
	public void setSearchRecord(Integer uid, String searchStr, int max) {
		try {
			ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
			String key = getSearchRecordKey(uid);
			zSetOperations.add(key, searchStr, System.currentTimeMillis());
			Long size = zSetOperations.size(key);
			if (Objects.nonNull(size) && size > max) {
				zSetOperations.reverseRangeWithScores(key, 0L, size - max - 1L);
			}
		} catch (Exception ignored) {
		}
	}

	public List<String> getSearchRecord(Integer uid) {
		return getSearchRecord(uid, 10);
	}

	/**
	 * 用户搜索历史
	 */
	public List<String> getSearchRecord(Integer uid, int max) {
		List<String> searchRecordList = new ArrayList<>();
		try {
			Set<ZSetOperations.TypedTuple<String>> set = stringRedisTemplate.opsForZSet().reverseRangeWithScores(getSearchRecordKey(uid), 0L, max - 1L);
			if (Objects.isNull(set)) return searchRecordList;
			set.forEach(e -> searchRecordList.add(e.getValue()));
		} catch (Exception ignored) {
		}
		return searchRecordList;
	}

	/**
	 * 删除用户搜索历史
	 */
	public void deleteSearchRecord(Integer uid) {
		stringRedisTemplate.delete(getSearchRecordKey(uid));
	}

}

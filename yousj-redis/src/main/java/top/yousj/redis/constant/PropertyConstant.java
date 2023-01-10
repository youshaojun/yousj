package top.yousj.redis.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static top.yousj.core.constant.PropertyConstant.PREFIX;

/**
 * @author yousj
 * @since 2023-01-08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyConstant {

	public static final String REDIS = PREFIX + "redis";

	public static final String MULTI_REDIS = PREFIX + "multi-redis";

}

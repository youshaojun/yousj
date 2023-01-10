package top.yousj.redis.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author yousj
 * @since 2022-12-29
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheConstant {

    /**
     * 1h
     */
    public static final String COMMON = "common";

    /**
     * 1d
     */
    public static final String LONG = "long";

    /**
     * 10m
     */
    public static final String SHORT = "short";

    /**
     * 自定义, 单位: 秒
     */
    public static final String CUSTOM = "custom";

}

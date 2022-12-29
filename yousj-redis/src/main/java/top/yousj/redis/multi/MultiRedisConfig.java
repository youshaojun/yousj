package top.yousj.redis.multi;

import lombok.Data;

import java.util.Map;


/**
 * @author yousj
 * @since 2022-12-29
 */
@Data
public class MultiRedisConfig {

    private Map<String, MultiRedisStandaloneConfiguration> redisConfigs;

}

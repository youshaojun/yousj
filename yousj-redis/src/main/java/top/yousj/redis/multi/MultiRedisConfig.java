package top.yousj.redis.multi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.yousj.redis.constant.PropertyConstant;

import java.util.Map;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Data
@Component
@ConfigurationProperties(prefix = PropertyConstant.MULTI_REDIS)
public class MultiRedisConfig {

    private Map<String, MultiRedisStandaloneConfiguration> configs;

}

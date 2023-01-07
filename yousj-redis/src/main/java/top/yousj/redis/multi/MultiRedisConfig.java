package top.yousj.redis.multi;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author yousj
 * @since 2022-12-29
 */
@Data
@Component
@ConfigurationProperties(prefix = "multi-redis")
public class MultiRedisConfig {

    private Map<String, MultiRedisStandaloneConfiguration> configs;

}

package top.yousj.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author yousj
 * @since 2022-12-29
 */
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnClass(RedisOperations.class)
public class RedissonConfig {

	@Bean
	@ConditionalOnMissingBean
	public RedissonClient redissonClient(RedisProperties redisProperties) {
		Config config = new Config()
			.setCodec(new StringCodec())
			.setTransportMode(TransportMode.NIO);
		config.useSingleServer()
			.setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort())
			.setDatabase(redisProperties.getDatabase())
			.setPassword(redisProperties.getPassword())
			.setConnectTimeout(
				Optional.ofNullable(redisProperties.getTimeout())
					.map(e -> e.get(ChronoUnit.MILLIS))
					.map(Long::intValue).orElse(10000));
		return Redisson.create(config);
	}

}
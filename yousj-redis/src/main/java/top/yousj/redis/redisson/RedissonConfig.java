package top.yousj.redis.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author yousj
 * @since 2022-12-29
 */
@Configuration
public class RedissonConfig {

	@Bean
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
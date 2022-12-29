package top.yousj.redis.multi;

import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

/**
 * @author yousj
 * @since 2022-12-29
 */
public class MultiRedisStandaloneConfiguration extends RedisStandaloneConfiguration {

	public MultiRedisStandaloneConfiguration(String hostName, String password, Integer port, Integer database) {
		PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
		mapper.from(hostName).to(super::setHostName);
		mapper.from(password).to(super::setPassword);
		mapper.from(port).to(super::setPort);
		mapper.from(database).to(super::setDatabase);
	}

}

package top.yousj.redis.multi;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import top.yousj.redis.RedisTemplateFactory;

/**
 * @author yousj
 * @since 2022-12-29
 */
public class MultiRedisTemplateFactoryBean implements FactoryBean<RedisTemplate<String, Object>> {

	private RedisStandaloneConfiguration standaloneConfiguration;

	public MultiRedisTemplateFactoryBean(RedisStandaloneConfiguration redisStandaloneConfiguration) {
		this.standaloneConfiguration = redisStandaloneConfiguration;
	}

	@Override
	public RedisTemplate<String, Object> getObject() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(this.standaloneConfiguration);
		jedisConnectionFactory.afterPropertiesSet();
		return RedisTemplateFactory.create(jedisConnectionFactory);
	}

	@Override
	public Class<?> getObjectType() {
		return RedisTemplate.class;
	}

}
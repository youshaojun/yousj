package top.yousj.redis.support;

import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.BatchStrategy;
import org.springframework.data.redis.cache.CacheStatisticsCollector;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.time.Duration;

public class RedisCacheWriterCustomize extends DefaultRedisCacheWriter {

    public RedisCacheWriterCustomize(RedisConnectionFactory connectionFactory, BatchStrategy batchStrategy) {
        super(connectionFactory, batchStrategy);
    }

    public RedisCacheWriterCustomize(RedisConnectionFactory connectionFactory, Duration sleepTime, BatchStrategy batchStrategy) {
        super(connectionFactory, sleepTime, CacheStatisticsCollector.none(), batchStrategy);
    }

    public RedisCacheWriterCustomize(RedisConnectionFactory connectionFactory, Duration sleepTime,
                              CacheStatisticsCollector cacheStatisticsCollector, BatchStrategy batchStrategy) {
        super(connectionFactory, sleepTime, cacheStatisticsCollector, batchStrategy);
    }

    /**
     * TODO do something after {@link org.springframework.data.redis.cache.DefaultRedisCacheWriter#put}
     */
    @Override
    public void put(String name, byte[] key, byte[] value, @Nullable Duration ttl) {
        super.put(name, key, value, ttl);
        // TODO do something.
    }

    public static RedisCacheWriter nonLockingRedisCacheWriter(RedisConnectionFactory connectionFactory) {
        return nonLockingRedisCacheWriter(connectionFactory, BatchStrategies.keys());
    }

   public static RedisCacheWriter nonLockingRedisCacheWriter(RedisConnectionFactory connectionFactory,
                                                       BatchStrategy batchStrategy) {

        Assert.notNull(connectionFactory, "ConnectionFactory must not be null!");
        Assert.notNull(batchStrategy, "BatchStrategy must not be null!");

        return new RedisCacheWriterCustomize(connectionFactory, batchStrategy);
    }

}

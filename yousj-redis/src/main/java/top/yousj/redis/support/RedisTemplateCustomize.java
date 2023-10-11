package top.yousj.redis.support;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisTemplateCustomize<K, V> extends RedisTemplate<K, V> {

    private final ValueOperations<K, V> valueOps = new ValueOperationsCustomize<>(this);

    @Override
    public ValueOperations<K, V> opsForValue() {
        return valueOps;
    }

}

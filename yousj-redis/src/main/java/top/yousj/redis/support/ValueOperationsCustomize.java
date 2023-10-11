package top.yousj.redis.support;

import org.springframework.data.redis.core.RedisTemplate;

public class ValueOperationsCustomize<K,V> extends DefaultValueOperations<K, V> {

    ValueOperationsCustomize(RedisTemplate<K, V> template) {
        super(template);
    }

    /**
     * TODO do something after {@link DefaultValueOperations#set(java.lang.Object, java.lang.Object)}
     */
    @Override
    public void set(K key, V value) {
        super.set(key, value);
        // do something.
    }

}

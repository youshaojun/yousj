package top.yousj.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.yousj.redis.support.RedisTemplateCustomize;

/**
 * @author yousj
 * @since 2022-12-29
 */
public class RedisTemplateFactory {

    public static RedisTemplate<String, Object> create(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        configuration(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    public static RedisTemplate<String, Object> customize(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplateCustomize = new RedisTemplateCustomize<>();
        configuration(redisTemplateCustomize, redisConnectionFactory);
        return redisTemplateCustomize;
    }

    private static void configuration(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory redisConnectionFactory) {
        RedisSerializer<String> keySerializer = RedisSerializer.string();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        Jackson2JsonRedisSerializer<Object> valueSerializer = getValueSerializer();
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
    }

    public static Jackson2JsonRedisSerializer<Object> getValueSerializer() {
        Jackson2JsonRedisSerializer<Object> valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        valueSerializer.setObjectMapper(objectMapper);
        return valueSerializer;
    }

}

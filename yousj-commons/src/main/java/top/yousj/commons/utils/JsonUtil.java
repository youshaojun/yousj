package top.yousj.commons.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JsonUtil {

	private static ObjectMapper objectMapper;

	@Autowired
	public JsonUtil(ObjectMapper objectMapper) {
		JsonUtil.objectMapper = objectMapper;
	}

	@SneakyThrows
	public static <T> String toJson(T object) {
		return objectMapper.writeValueAsString(object);
	}

	@SneakyThrows
	public static <T> T fromJson(String json, Class<T> clazz) {
		return objectMapper.readValue(json, clazz);
	}

	@SneakyThrows
	public static <T> T fromJson(String json, TypeReference<T> typeReference) {
		return objectMapper.readValue(json, typeReference);
	}

	public static boolean isJson(String json) {
		return Try.of(() -> {
			objectMapper.readTree(json);
			return true;
		}).getOrElse(false);
	}

	@SuppressWarnings("unchecked")
	public static String fromJsonMap(String json, String key) {
		return Try.of(() -> ((Map<String, String>) fromJson(json, Map.class)).get(key)).getOrElse(json);
	}

}
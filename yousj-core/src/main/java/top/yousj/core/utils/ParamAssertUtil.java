package top.yousj.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import top.yousj.core.exception.BusinessException;

import java.util.Collection;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParamAssertUtil {

	public static void notNull(Object data, String message) {
		notNull(data, new BusinessException(message));
	}

	public static void notNull(Object data, RuntimeException exception) {
		if (Objects.isNull(data)) {
			throw exception;
		}
	}

	public static void notBlank(String data, RuntimeException exception) {
		if (StringUtils.isBlank(data)) {
			throw exception;
		}
	}

	public static void notBlank(String data, String message) {
		notBlank(data, new BusinessException(message));
	}

	public static void isBlank(String data, String message) {
		isBlank(data, new BusinessException(message));
	}

	public static void isBlank(String data, RuntimeException exception) {
		if (StringUtils.isNotBlank(data)) {
			throw exception;
		}
	}

	public static void notEmpty(Collection data, RuntimeException exception) {
		if (CollectionUtils.isEmpty(data)) {
			throw exception;
		}
	}

	public static void notEmpty(Collection data, String message) {
		if (CollectionUtils.isEmpty(data)) {
			notEmpty(data, new BusinessException(message));
		}
	}

	public static void isTrue(boolean isTrue, String message) {
		isFalse(BooleanUtils.negate(isTrue), message);
	}

	public static void isTrue(boolean isTrue, RuntimeException exception) {
		isFalse(BooleanUtils.negate(isTrue), exception);
	}

	public static void isFalse(boolean isTrue, String message) {
		if (isTrue) {
			throw new BusinessException(message);
		}
	}

	public static void isFalse(boolean isTrue, RuntimeException exception) {
		if (isTrue) {
			throw exception;
		}
	}
}

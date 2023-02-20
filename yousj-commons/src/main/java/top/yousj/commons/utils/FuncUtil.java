package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author yousj
 * @since 2023-02-10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuncUtil {

	public static <T> T call(Supplier<T> supplier) {
		return call(supplier, null);
	}

	public static <T> T call(Supplier<T> supplier, T defaultValue) {
		try {
			return supplier.get();
		} catch (Exception ignored) {
		}
		return defaultValue;
	}

	public static <T> T callIfNotNull(Object o, Supplier<T> supplier) {
		return conditionCall(Objects.nonNull(o), supplier);
	}

	public static <T> T conditionCall(boolean condition, Supplier<T> supplier) {
		if (condition) {
			return supplier.get();
		}
		return null;
	}

}

package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.function.Consumer;
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

	public static <T> void conditionCall(boolean condition, Supplier<T> supplier) {
		if (condition) {
			supplier.get();
		}
	}

	public static <T> void callIfNotNull(T t, Consumer<T> consumer) {
		if (Objects.nonNull(t)) {
			consumer.accept(t);
		}
	}

}

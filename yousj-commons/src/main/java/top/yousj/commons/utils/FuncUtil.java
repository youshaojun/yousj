package top.yousj.commons.utils;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
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

	public static <T> T conditionCall(boolean condition, CheckedFunction0<? extends T> supplier) {
		return condition ? Try.of(supplier).getOrNull() : null;
	}

	public static <T> T callIfNotNull(Object o, CheckedFunction0<? extends T> supplier) {
		return conditionCall(Objects.nonNull(o), supplier);
	}

	public static <T> void callIfNotNull(T t, Consumer<T> consumer) {
		if (Objects.nonNull(t)) {
			Try.run(() -> consumer.accept(t));
		}
	}

	public static void runnable(boolean condition, Runnable runnable) {
		if (condition) {
			Try.runRunnable(runnable);
		}
	}

	public static void runnable(boolean condition, Runnable runnable, Runnable orElse) {
		Try.runRunnable(condition ? runnable : orElse);
	}

	public static <T> T supplier(boolean condition, Supplier<T> supplier, Supplier<T> orElse) {
		return Try.ofSupplier(condition ? supplier : orElse).getOrNull();
	}

}

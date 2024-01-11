package top.yousj.commons.utils;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author yousj
 * @since 2023-02-10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuncUtil {

    public static <T> T call(boolean condition, CheckedFunction0<? extends T> supplier) {
        return condition ? Try.of(supplier).getOrNull() : null;
    }

    public static <T> T callIfNotNull(Object o, CheckedFunction0<? extends T> supplier) {
        return call(Objects.nonNull(o), supplier);
    }

    public static <T> void callIfNotNull(T t, Consumer<T> consumer) {
        if (Objects.nonNull(t)) {
            Try.run(() -> consumer.accept(t));
        }
    }

    public static void run(boolean condition, Runnable runnable) {
        if (condition) {
            Try.runRunnable(runnable);
        }
    }

    public static void run(boolean condition, Runnable runnable, Runnable orElse) {
        Try.runRunnable(condition ? runnable : orElse);
    }

    public static <T> T supplier(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    public static <T> T supplier(boolean condition, Supplier<T> supplier, Supplier<T> orElse) {
        return Try.ofSupplier(condition ? supplier : orElse).getOrNull();
    }

    public static <T, K> Map<K, T> toMap(List<T> list, Function<? super T, ? extends K> keyMapper) {
        return toMap(list, keyMapper, e -> e);
    }

    public static <T, K, U> Map<K, U> toMap(List<T> list, Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.stream().collect(Collectors.toMap(keyMapper, valueMapper, (k1, k2) -> k1));
    }

}

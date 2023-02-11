package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

/**
 * @author yousj
 * @since 2023-02-10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FuncUtil {

	public static <T> T call(Supplier<T> supplier) {
		try {
			return supplier.get();
		} catch (Exception ignored) {
		}
		return null;
	}

}

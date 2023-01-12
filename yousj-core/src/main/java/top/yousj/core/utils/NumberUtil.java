package top.yousj.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yousj
 * @since 2023-01-12
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {

	public static boolean gt0(BigDecimal value) {
		return Objects.nonNull(value) && value.compareTo(BigDecimal.ZERO) > 0;
	}

	public static boolean gt0(Long value) {
		return Objects.nonNull(value) && value > 0;
	}

	public static boolean gt0(Integer value) {
		return Objects.nonNull(value) && value > 0;
	}

	private Integer sum(Integer... params) {
		return Arrays.stream(Optional.ofNullable(params).orElse(new Integer[]{})).filter(Objects::nonNull).reduce(Integer::sum).orElse(0);
	}

	private Long sum(Long... params) {
		return Arrays.stream(Optional.ofNullable(params).orElse(new Long[]{})).filter(Objects::nonNull).reduce(Long::sum).orElse(0L);
	}

}

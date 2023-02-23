package top.yousj.commons.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

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

}

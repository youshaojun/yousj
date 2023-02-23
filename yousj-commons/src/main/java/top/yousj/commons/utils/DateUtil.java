package top.yousj.commons.utils;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import top.yousj.commons.constant.StrPool;
import top.yousj.commons.date.DatePattern;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * 日期工具
 *
 * @author yousj
 * @since 2023-01-05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

	public static Date forever() {
		return DateUtils.addMilliseconds(new Date(), Integer.MAX_VALUE);
	}

	public static Date parseDate(String date) {
		return Try.of(() -> DateUtils.parseDate(date, DatePattern.PATTERN)).getOrNull();
	}

	public static String format(Date date) {
		return format(date, DatePattern.SIMPLE_DATETIME_PATTERN);
	}

	public static String format(Date date, String format) {
		return Try.of(() -> DateFormatUtils.format(date, format)).getOrNull();
	}

	public static LocalDate date2LocalDate(Date date) {
		return Try.of(() -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getOrNull();
	}

	public static Date localDate2Date(LocalDate localDate) {
		return Try.of(() -> Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())).getOrNull();
	}

	public static String join(Date start, Date end) {
		return join(start, end, "至", DatePattern.SIMPLE_DATETIME_PATTERN);
	}

	public static String join(Date start, Date end, String delimiter, String format) {
		return Try.of(() -> Objects.isNull(end) ? format(start, format) : StringUtils.join(delimiter, Objects.isNull(start) ? StrPool.EMPTY : format(start, format), format(end, format))).getOrNull();
	}

}

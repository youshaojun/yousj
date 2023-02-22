package top.yousj.commons.utils;

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
		return FuncUtil.callIfNotNull(date, () -> {
			try {
				return DateUtils.parseDate(date, DatePattern.PATTERN);
			} catch (Exception ignored) {
			}
			return null;
		});
	}

	public static String format(Date date) {
		return format(date, DatePattern.SIMPLE_DATETIME_PATTERN);
	}

	public static String format(Date date, String format) {
		return FuncUtil.callIfNotNull(date, () -> DateFormatUtils.format(date, format));
	}

	public static LocalDate date2LocalDate(Date date) {
		return FuncUtil.callIfNotNull(date, () -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
	}

	public static Date localDate2Date(LocalDate localDate) {
		return FuncUtil.callIfNotNull(localDate, () -> Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}

	public static String join(Date start, Date end) {
		return join(start, end, "至", DatePattern.SIMPLE_DATETIME_PATTERN);
	}

	public static String join(Date start, Date end, String delimiter, String format) {
		return FuncUtil.call(() -> Objects.isNull(end) ? format(start, format) : StringUtils.join(delimiter, Objects.isNull(start) ? StrPool.EMPTY : format(start, format), format(end, format)), StrPool.EMPTY);
	}

}

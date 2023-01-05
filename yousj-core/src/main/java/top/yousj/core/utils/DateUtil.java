package top.yousj.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class DateUtil {

	private static final String[] PATTERN = {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy年MM月dd日", "yyyyMMdd"};


	public static Date forever() {
		return DateUtils.addMilliseconds(new Date(), Integer.MAX_VALUE);
	}

	public static Date parseDate(String date) {
		try {
			return DateUtils.parseDate(date, PATTERN);
		} catch (Exception ignored) {
		}
		return null;
	}

	public static String format(Date date) {
		return format(date, "yyyy-MM-dd");
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.format(date);
		} catch (Exception ignored) {
		}
		return null;
	}

	public static LocalDate date2LocalDate(Date date) {
		if (date == null) {
			return null;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		return date.toInstant().atZone(zoneId).toLocalDate();
	}

	public static Date localDate2Date(LocalDate localDate) {
		if (localDate == null) {
			return null;
		}
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static String join(Date start, Date end) {
		return join(start, end, "至", "yyyy-MM-dd");
	}

	public static String join(Date start, Date end, String delimiter, String format) {
		try {
			if (Objects.isNull(end)) {
				return format(start, format);
			}
			return StringUtils.join(delimiter, Objects.isNull(start) ? "" : format(start, format), format(end, format));
		} catch (Exception ignored) {
		}
		return StringUtils.EMPTY;
	}

}

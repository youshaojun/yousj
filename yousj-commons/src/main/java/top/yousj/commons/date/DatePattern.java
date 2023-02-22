package top.yousj.commons.date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

/**
 * @author yousj
 * @since 2023-01-11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DatePattern {

	public static final String SIMPLE_DATETIME_PATTERN = "yyyy-MM-dd";
	public static final String NORM_DATETIME_PATTERN = SIMPLE_DATETIME_PATTERN + " HH:mm:ss";
	public static final DateTimeFormatter NORM_DATETIME_FORMATTER = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN);
	public static final String UTC_SIMPLE_PATTERN = SIMPLE_DATETIME_PATTERN + "'T'HH:mm:ss";
	public static final DateTimeFormatter UTC_SIMPLE_FORMATTER = DateTimeFormatter.ofPattern(UTC_SIMPLE_PATTERN);
	public static final String[] PATTERN = {SIMPLE_DATETIME_PATTERN, NORM_DATETIME_PATTERN, "yyyy-MM-dd HH:mm", "yyyy/MM/dd", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy年MM月dd日", "yyyyMMdd"};

}
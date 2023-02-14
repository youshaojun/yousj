package top.yousj.log.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static top.yousj.commons.constant.PropertyConstant.PREFIX;

/**
 * @author yousj
 * @since 2023-01-08
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyConstant {

	public static final String LOG = PREFIX + "log";
	public static final String TRACE_ID = "X-Trace-Id";

}

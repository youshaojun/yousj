package top.yousj.core.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

public class DateUtil {

	public static Date forever(){
		return DateUtils.addMilliseconds(new Date(), Integer.MAX_VALUE);
	}

}

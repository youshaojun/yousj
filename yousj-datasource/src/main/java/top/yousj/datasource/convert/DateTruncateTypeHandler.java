package top.yousj.datasource.convert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({Date.class})
public class DateTruncateTypeHandler extends DateTypeHandler {

	public DateTruncateTypeHandler() {
	}

	public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
		super.setNonNullParameter(ps, i, DateUtils.truncate(parameter, Calendar.SECOND), jdbcType);
	}
}
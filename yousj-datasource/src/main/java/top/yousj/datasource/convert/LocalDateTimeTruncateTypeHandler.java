package top.yousj.datasource.convert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes({LocalDateTime.class})
public class LocalDateTimeTruncateTypeHandler extends LocalDateTimeTypeHandler {

	public LocalDateTimeTruncateTypeHandler() {
	}

	public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
		super.setNonNullParameter(ps, i, parameter.truncatedTo(ChronoUnit.SECONDS), jdbcType);
	}
}
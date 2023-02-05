package top.yousj.datasource.utils;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author yousj
 * @since 2023-01-30
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SqlUtil {

	public static final String MAIN = "/**main**/";

	public static String getColumnScript(TableInfo tableInfo) {
		return SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlColumnMaybeIf(null), StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET, null, StringPool.COMMA);
	}

	public static String getValuesScript(TableInfo tableInfo) {
		return SqlScriptUtils.convertTrim(tableInfo.getAllInsertSqlPropertyMaybeIf(null), StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET, null, StringPool.COMMA);
	}

	public static AbstractWrapper wrapWithMain(AbstractWrapper wrapper) {
		wrapper.first(MAIN);
		return wrapper;
	}

}

package top.yousj.datasource.injector.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.yousj.datasource.injector.enums.SqlMethod;

import static top.yousj.datasource.utils.InjectorSqlUtil.*;

public class ReplaceInto extends AbstractMethod {

	public ReplaceInto() {
		super(SqlMethod.REPLACE_INTO.getMethod());
	}

	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		String sql = String.format(SqlMethod.REPLACE_INTO.getSql(), tableInfo.getTableName(), getColumnScript(tableInfo), getValuesScript(tableInfo));
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return this.addInsertMappedStatement(mapperClass, modelClass, SqlMethod.REPLACE_INTO.getMethod(), sqlSource, new NoKeyGenerator(), tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
	}

}
package top.yousj.datasource.injector.methods;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import top.yousj.datasource.injector.enums.SqlMethod;

import static top.yousj.datasource.utils.InjectorSqlUtil.*;

public class InsertOnDuplicateKeyUpdate extends AbstractMethod {

	public InsertOnDuplicateKeyUpdate() {
		super(SqlMethod.INSERT_ON_DUPLICATE_KEY_UPDATE.getMethod());
	}

	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
		SqlMethod sqlMethod = SqlMethod.INSERT_ON_DUPLICATE_KEY_UPDATE;
		String keyProperty = null;
		String keyColumn = null;
		if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
			if (tableInfo.getIdType() == IdType.AUTO) {
				keyGenerator = Jdbc3KeyGenerator.INSTANCE;
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			} else if (null != tableInfo.getKeySequence()) {
				keyGenerator = TableInfoHelper.genKeyGenerator(sqlMethod.getMethod(), tableInfo, builderAssistant);
				keyProperty = tableInfo.getKeyProperty();
				keyColumn = tableInfo.getKeyColumn();
			}
		}
		String sql = String.format(sqlMethod.getSql(), tableInfo.getTableName(), getColumnScript(tableInfo), getValuesScript(tableInfo), SqlScriptUtils.convertTrim(tableInfo.getAllSqlSet(tableInfo.isWithLogicDelete(), StringPool.EMPTY), null, null, null, StringPool.COMMA));
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		return addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource, keyGenerator, keyProperty, keyColumn);
	}

}

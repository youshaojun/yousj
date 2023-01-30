package top.yousj.datasource.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * @author yousj
 * @since 2023-01-30
 */
public interface CustomizeBaseMapper<T> extends BaseMapper<T> {

	int replaceInto(T entity);

	int insertOnDuplicateKeyUpdate(T entity);

	default T getOneWithMain(@Param(Constants.WRAPPER) LambdaQueryWrapper<T> lambdaQueryWrapper) {
		lambdaQueryWrapper.first("/*main*/");
		return selectOne(lambdaQueryWrapper);
	}

}

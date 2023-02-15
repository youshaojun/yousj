package top.yousj.datasource.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author yousj
 * @since 2023-01-30
 */
public interface ICustomizeBaseMapper<T> extends BaseMapper<T> {

	int replaceInto(T entity);

	int insertOnDuplicateKeyUpdate(T entity);

}

package top.yousj.datasource.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.yousj.datasource.mapper.ICustomizeBaseMapper;
import top.yousj.datasource.utils.SqlUtil;

/**
 * @author yousj
 * @since 2023-02-06
 */
public class CustomizeServiceImpl<M extends ICustomizeBaseMapper<T>, T> extends ServiceImpl<M, T> implements ICustomizeService<T> {

	@Override
	public int replaceInto(T entity) {
		return baseMapper.replaceInto(entity);
	}

	@Override
	public int insertOnDuplicateKeyUpdate(T entity) {
		return baseMapper.insertOnDuplicateKeyUpdate(entity);
	}

	@Override
	public T getOneWithMain(Wrapper<T> wrapper) {
		return baseMapper.selectOne(SqlUtil.withMain(wrapper));
	}

}

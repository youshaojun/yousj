package top.yousj.datasource.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author yousj
 * @since 2023-02-06
 */
public interface ICustomizeService<T> extends IService<T> {

	int replaceInto(T entity);

	int insertOnDuplicateKeyUpdate(T entity);

}

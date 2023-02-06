package top.yousj.uaa.service.impl;

import top.yousj.datasource.service.ICustomizeServiceImpl;
import top.yousj.uaa.entity.po.UaaUserDataSource;
import top.yousj.uaa.mapper.UaaUserDataSourceMapper;
import top.yousj.uaa.service.IUaaUserDataSourceService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yousj
 * @since 2023-01-15
 */
@Service
public class UaaUserDataSourceServiceImpl extends ICustomizeServiceImpl<UaaUserDataSourceMapper, UaaUserDataSource> implements IUaaUserDataSourceService {

}

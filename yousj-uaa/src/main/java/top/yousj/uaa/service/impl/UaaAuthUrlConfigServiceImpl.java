package top.yousj.uaa.service.impl;

import top.yousj.datasource.service.CustomizeServiceImpl;
import top.yousj.uaa.entity.po.UaaAuthUrlConfig;
import top.yousj.uaa.mapper.UaaAuthUrlConfigMapper;
import top.yousj.uaa.service.IUaaAuthUrlConfigService;
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
public class UaaAuthUrlConfigServiceImpl extends CustomizeServiceImpl<UaaAuthUrlConfigMapper, UaaAuthUrlConfig> implements IUaaAuthUrlConfigService {

}

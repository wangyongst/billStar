package com.tuofan.configs.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.configs.entity.SysConfigs;
import com.tuofan.configs.mapper.SysConfigsMapper;
import com.tuofan.configs.service.ISysConfigsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 配置表 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-21
 */
@Service
public class SysConfigsServiceImpl extends ServiceImpl<SysConfigsMapper, SysConfigs> implements ISysConfigsService {

}

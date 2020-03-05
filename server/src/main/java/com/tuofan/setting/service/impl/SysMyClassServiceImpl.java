package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysMyClass;
import com.tuofan.setting.mapper.SysMyClassMapper;
import com.tuofan.setting.service.ISysMyClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-03-04
 */
@Service
public class SysMyClassServiceImpl extends ServiceImpl<SysMyClassMapper, SysMyClass> implements ISysMyClassService {

    @Override
    public List listV() {
        return baseMapper.listV();
    }
}

package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysClass;
import com.tuofan.setting.mapper.SysClassMapper;
import com.tuofan.setting.service.ISysClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.ClassV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
@Service
public class SysClassServiceImpl extends ServiceImpl<SysClassMapper, SysClass> implements ISysClassService {

    @Override
    public List<ClassV> listV() {
        return baseMapper.listV();
    }
}

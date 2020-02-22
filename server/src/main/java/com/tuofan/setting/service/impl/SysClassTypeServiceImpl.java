package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysClassType;
import com.tuofan.setting.mapper.SysClassTypeMapper;
import com.tuofan.setting.service.ISysClassTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.ClassTypeV;
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
public class SysClassTypeServiceImpl extends ServiceImpl<SysClassTypeMapper, SysClassType> implements ISysClassTypeService {

    @Override
    public List<ClassTypeV> listV() {
        return baseMapper.listV();
    }
}

package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysMySchool;
import com.tuofan.setting.mapper.SysMySchoolMapper;
import com.tuofan.setting.service.ISysMySchoolService;
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
public class SysMySchoolServiceImpl extends ServiceImpl<SysMySchoolMapper, SysMySchool> implements ISysMySchoolService {

    @Override
    public List listV() {
        return baseMapper.listV();
    }
}

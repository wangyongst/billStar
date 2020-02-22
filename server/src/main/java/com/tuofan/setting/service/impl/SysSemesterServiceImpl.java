package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysSemester;
import com.tuofan.setting.mapper.SysSemesterMapper;
import com.tuofan.setting.service.ISysSemesterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.SemesterV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
@Service
public class SysSemesterServiceImpl extends ServiceImpl<SysSemesterMapper, SysSemester> implements ISysSemesterService {

    @Override
    public List<SemesterV> listV() {
        return baseMapper.listV();
    }

    @Override
    public void allNotDefault() {
        baseMapper.allNotDefault();
    }

}

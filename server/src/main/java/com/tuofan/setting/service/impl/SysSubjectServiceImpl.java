package com.tuofan.setting.service.impl;

import com.tuofan.setting.entity.SysSubject;
import com.tuofan.setting.mapper.SysSubjectMapper;
import com.tuofan.setting.service.ISysSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.setting.vo.SubjectV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
@Service
public class SysSubjectServiceImpl extends ServiceImpl<SysSubjectMapper, SysSubject> implements ISysSubjectService {

    @Override
    public List<SubjectV> listV() {
        return baseMapper.listV();
    }
}

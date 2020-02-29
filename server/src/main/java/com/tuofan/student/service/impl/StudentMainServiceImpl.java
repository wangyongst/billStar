package com.tuofan.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.mapper.StudentMainMapper;
import com.tuofan.student.service.IStudentMainService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生信息 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@Service
public class StudentMainServiceImpl extends ServiceImpl<StudentMainMapper, StudentMain> implements IStudentMainService {

    @Override
    public IPage pageV(IPage page, QueryWrapper queryWrapper) {
        return baseMapper.pageV(page, queryWrapper);
    }
}

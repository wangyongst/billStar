package com.tuofan.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.mapper.StudentCourseMapper;
import com.tuofan.student.service.IStudentCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tuofan.student.vo.StudentCourseV;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 学生-课程记录 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@Service
public class StudentCourseServiceImpl extends ServiceImpl<StudentCourseMapper, StudentCourse> implements IStudentCourseService {

    @Override
    public IPage pageV(IPage page, QueryWrapper queryWrapper) {
        return baseMapper.pageV(page, queryWrapper);
    }

    @Override
    public List<StudentCourseV> listStudentCourseV(QueryWrapper queryWrapper) {
        return baseMapper.listStudentCourseV(queryWrapper);
    }
}

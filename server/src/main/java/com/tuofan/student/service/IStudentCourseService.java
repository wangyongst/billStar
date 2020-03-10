package com.tuofan.student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.student.entity.StudentCourse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学生-课程记录 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
public interface IStudentCourseService extends IService<StudentCourse> {

    IPage pageV(IPage page, QueryWrapper queryWrapper);

}

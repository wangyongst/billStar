package com.tuofan.course.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.course.entity.CourseMain;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程信息 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
public interface ICourseMainService extends IService<CourseMain> {

    IPage pageV(IPage page, QueryWrapper queryWrapper);
}

package com.tuofan.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.mapper.CourseMainMapper;
import com.tuofan.course.service.ICourseMainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程信息 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
@Service
public class CourseMainServiceImpl extends ServiceImpl<CourseMainMapper, CourseMain> implements ICourseMainService {

    @Override
    public IPage pageV(IPage page, QueryWrapper queryWrapper) {
        return baseMapper.pageV(page,queryWrapper);
    }
}

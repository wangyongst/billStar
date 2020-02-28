package com.tuofan.course.service.impl;

import com.tuofan.course.entity.CourseTime;
import com.tuofan.course.mapper.CourseTimeMapper;
import com.tuofan.course.service.ICourseTimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 票据信息 服务实现类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-28
 */
@Service
public class CourseTimeServiceImpl extends ServiceImpl<CourseTimeMapper, CourseTime> implements ICourseTimeService {

}

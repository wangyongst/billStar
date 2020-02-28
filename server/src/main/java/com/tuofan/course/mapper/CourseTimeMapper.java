package com.tuofan.course.mapper;

import com.tuofan.course.entity.CourseTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 票据信息 Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-28
 */
public interface CourseTimeMapper extends BaseMapper<CourseTime> {

    @Select("select * from course_time where id in ( ${ids} )")
    List<CourseTime> listByIdIn(String ids);

}

package com.tuofan.course.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.course.entity.CourseMain;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.course.vo.CourseV;
import com.tuofan.setting.vo.ClassV;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 课程信息 Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
public interface CourseMainMapper extends BaseMapper<CourseMain> {

    @Select("select course.*,subject.name subject_name,school.name school_name,semester.name semester_name \n" +
            "from course_main course \n" +
            "join sys_class class on course.class_id = class.id \n" +
            "join sys_subject subject on class.subject_id = subject.id \n" +
            "join ding_dept school on course.school_id = school.id \n" +
            "join ding_user teacher on course.teacher_id = teacher.id\n" +
            "join sys_semester semester on course.semester_id = semester.id ${ew.customSqlSegment}")
    IPage<CourseV> pageV(IPage ipage, @Param(Constants.WRAPPER) QueryWrapper wrapper);


    @Select("select course.*,subject.name subject_name,school.name school_name,semester.name semester_name,courseTime.*\n" +
            "from course_main course\n" +
            "left join sys_class class on course.class_id = class.id\n" +
            "left join sys_subject subject on class.subject_id = subject.id\n" +
            "left join ding_dept school on course.school_id = school.id\n" +
            "left join ding_user teacher on course.teacher_id = teacher.id\n" +
            "left join sys_semester semester on course.semester_id = semester.id\n" +
            "right join course_time courseTime on courseTime.id = (course.time_ids)\n ${ew.customSqlSegment}")
    List<CourseV> listV(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}

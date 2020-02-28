package com.tuofan.student.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tuofan.student.entity.StudentCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.student.vo.StudentCourseV;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 学生-课程记录 Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
public interface StudentCourseMapper extends BaseMapper<StudentCourse> {


    @Select("select studentCourse.*,school.name school_name,student.name student_name,teacher.name teacher_name,course.class_no,course.class_room\n" +
            "from student_course studentCourse \n" +
            "left join student_main student on studentCourse.student_id = student.id \n" +
            "left join course_main course on studentCourse.course_id = course.id \n" +
            "left join sys_class class on course.class_id = class.id\n" +
            "left join sys_subject subject on class.subject_id = subject_id  \n" +
            "left join ding_user teacher on course.teacher_id = teacher.id \n" +
            "left join ding_dept school on course.school_id = school.id ${ew.customSqlSegment}")
    IPage<StudentCourseV> pageV(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}

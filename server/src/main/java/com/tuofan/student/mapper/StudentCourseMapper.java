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

    @Select("select studentCourse.*,course.*,student.*,school.name school_name\n" +
            "from student_course studentCourse\n" +
            "join course_main course on studentCourse.course_id = course.id\n" +
            "join student_main student on studentCourse.student_id = student.id\n" +
            "join ding_dept school on student.school_id = school.id ${ew.customSqlSegment}")
    IPage<StudentCourseV> pageV(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}

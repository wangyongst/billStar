package com.tuofan.student.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tuofan.student.entity.StudentCharge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.student.vo.StudentChargeV;
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
public interface StudentChargeMapper extends BaseMapper<StudentCharge> {

    @Select("select studentCourse.expire_time,school.name school_name,student.name student_name,teacher.name teacher_name,user.name createName,charge.create_time\n" +
            "from student_charge charge \n" +
            "left join student_main student on charge.student_id = student.id\n" +
            "left join student_course studentCourse on student.id = studentCourse.student_id\n" +
            "left join course_main course on studentCourse.course_id = course.id\n" +
            "left join sys_class class on course.class_id = class.id\n" +
            "left join sys_subject subject on class.subject_id = subject_id  \n" +
            "left join ding_user teacher on course.teacher_id = teacher.id \n" +
            "left join ding_dept school on course.school_id = school.id\n" +
            "left join ding_user user on charge.create_by = user.id ${ew.customSqlSegment}")
    IPage<StudentChargeV> pageV(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);

}

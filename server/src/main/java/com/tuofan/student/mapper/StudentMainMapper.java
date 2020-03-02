package com.tuofan.student.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.vo.StudentCourseV;
import com.tuofan.student.vo.StudentMainV;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 学生信息 Mapper 接口
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
public interface StudentMainMapper extends BaseMapper<StudentMain> {

    @Select("select student.*,school.name school_name,user.name create_name\n" +
            "from student_main student\n" +
            "join ding_dept school on student.school_id = school.id\n" +
            "join ding_user user on student.create_by = user.id ${ew.customSqlSegment}")
    IPage<StudentMainV> pageV(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}

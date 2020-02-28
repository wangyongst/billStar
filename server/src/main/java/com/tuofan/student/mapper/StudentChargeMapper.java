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
    @Select("select charge.*,student.name student_name,school.name school_name,user.name create_name,syscharge.name charge_name\n" +
            "from student_charge charge\n" +
            "join student_main student on student.id = charge.student_id\n" +
            "join ding_dept school on student.school_id = school.id\n" +
            "join ding_user user on user.id = charge.create_by\n" +
            "join sys_charge syscharge on charge.charge_id = syscharge.id ${ew.customSqlSegment}")
    IPage<StudentChargeV> pageV(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}

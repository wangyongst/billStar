package com.tuofan.student.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.tuofan.report.vo.ChargeReportV;
import com.tuofan.student.entity.StudentCharge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tuofan.student.vo.BillV;
import com.tuofan.student.vo.ChargeV;
import com.tuofan.student.vo.StudentChargeV;
import com.tuofan.student.vo.StudentCourseV;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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


    @Select("select school.name school_name,type.name charge_type,sum(charge.amount) sum\n" +
            "from student_charge charge\n" +
            "join student_main student on charge.student_id = student.id\n" +
            "join ding_dept school on school.id = student.school_id\n" +
            "join sys_charge type on type.id = charge.charge_id\n" +
            "${ew.customSqlSegment}\n" +
            "group by school.name,type.id")
    IPage<ChargeReportV> reportV(IPage page, @Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    @Select("select date_format(charge.create_time, '%Y-%m') month,school.name school_name,sum(charge.amount) sum\n" +
            "from student_charge charge\n" +
            "join student_main student on charge.student_id = student.id\n" +
            "join ding_dept school on school.id = student.school_id\n" +
            "join sys_charge type on type.id = charge.charge_id\n" +
            "${ew.customSqlSegment}\n" +
            "group by school.name,month")
    List<ChargeReportV> reportMonth(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("select date_format(charge.create_time, '%Y-%m') month,sum(charge.amount) sum\n" +
            "from student_charge charge\n" +
            "join student_main student on charge.student_id = student.id\n" +
            "join ding_dept school on school.id = student.school_id\n" +
            "join sys_charge type on type.id = charge.charge_id\n" +
            "${ew.customSqlSegment}\n" +
            "group by month")
    List<ChargeReportV> reportMonthTotal(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);


    @Select("select studentCharge.*,student.name student_name,student.mobile mobile,student.school_id\n" +
            "from student_charge studentCharge\n" +
            "left join student_main student on studentCharge.student_id = student.id\n" +
            "where studentCharge.id = #{id}")
    BillV getByChargeId(Integer id);

    @Select("select studentCharge.*,SysCharge.*,SysCharge.name charge_name\n" +
            "from student_charge studentCharge\n" +
            "left join sys_charge SysCharge on studentCharge.charge_id= SysCharge.id ${ew.customSqlSegment}")
    List<StudentChargeV> listStudentChargeV(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("select type.name charge_type,school.name school_name,sum(charge.amount) sum\n" +
            "from student_charge charge\n" +
            "join student_main student on charge.student_id = student.id\n" +
            "join ding_dept school on school.id = student.school_id\n" +
            "join sys_charge type on type.id = charge.charge_id\n" +
            "${ew.customSqlSegment}\n" +
            "group by school.name,charge_type")
    List<ChargeReportV> reportChargeType(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    @Select("select type.name charge_type,sum(charge.amount) sum\n" +
            "from student_charge charge\n" +
            "join student_main student on charge.student_id = student.id\n" +
            "join ding_dept school on school.id = student.school_id\n" +
            "join sys_charge type on type.id = charge.charge_id\n" +
            "${ew.customSqlSegment}\n" +
            "group by charge_type")
    List<ChargeReportV> reportChargeTypeTotal(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);
}

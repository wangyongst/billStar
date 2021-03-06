package com.tuofan.student.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tuofan.report.vo.ChargeReportV;
import com.tuofan.student.entity.StudentCharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.student.vo.BillV;
import com.tuofan.student.vo.StudentChargeV;

import java.util.List;

/**
 * <p>
 * 学生-课程记录 服务类
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
public interface IStudentChargeService extends IService<StudentCharge> {

    IPage pageV(IPage page, QueryWrapper queryWrapper);

    BillV getByChargeId(Integer id);

    List<ChargeReportV> reportMonth(QueryWrapper queryWrapper);

    List<ChargeReportV> reportMonthTotal(QueryWrapper queryWrapper);

    List<ChargeReportV> reportChargeType(QueryWrapper queryWrapper);

    List<ChargeReportV> reportChargeTypeTotal(QueryWrapper queryWrapper);

    List<StudentChargeV> listStudentChargeV(QueryWrapper queryWrapper);

}

package com.tuofan.report.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.report.vo.ChargeReportQ;
import com.tuofan.setting.service.ISysChargeService;
import com.tuofan.student.service.IStudentChargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
@Slf4j
@RestController
@RequestMapping("/bill/report")
public class ReportController {

    @Autowired
    private ISysChargeService iSysChargeService;

    @Autowired
    private IStudentChargeService iStudentChargeService;

    @PostMapping("charge")
    public Result list(@RequestBody ChargeReportQ chargeReportQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null && chargeReportQ.getEnd() != null) queryWrapper.between("charge.create_time", chargeReportQ.getBegin(), chargeReportQ.getEnd());
        return Result.ok(iStudentChargeService.reportV(new Page(chargeReportQ.getCurrent(), chargeReportQ.getSize()), queryWrapper));
    }

    @PostMapping("month")
    public Result month(@RequestBody ChargeReportQ chargeReportQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null) queryWrapper.ge("charge.create_time", chargeReportQ.getBegin());
        return Result.ok(iStudentChargeService.reportMonth(new Page(chargeReportQ.getCurrent(), chargeReportQ.getSize()), queryWrapper));
    }
}


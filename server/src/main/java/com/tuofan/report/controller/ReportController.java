package com.tuofan.report.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.core.Result;
import com.tuofan.report.vo.ChargeReportQ;
import com.tuofan.report.vo.ChargeReportV;
import com.tuofan.report.vo.YearHeaderV;
import com.tuofan.setting.service.ISysChargeService;
import com.tuofan.student.service.IStudentChargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
        IPage page = iStudentChargeService.reportMonth(new Page(chargeReportQ.getCurrent(), chargeReportQ.getSize()), queryWrapper);
        page.getRecords().stream().map(e -> setType((ChargeReportV) e, "完成")).collect(Collectors.toList());
        return Result.ok(page);
    }

    public ChargeReportV setType(ChargeReportV crv, String type) {
        crv.setType(type);
        return crv;
    }

    @PostMapping("header")
    public Result header(@RequestBody ChargeReportQ chargeReportQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null) queryWrapper.ge("charge.create_time", chargeReportQ.getBegin());
        List<ChargeReportV> crv = iStudentChargeService.reportMonth(new Page(1, 100), queryWrapper).getRecords();
        List<YearHeaderV> headerList = Lists.newArrayList();
        headerList.add(makeHeader("校区", "schoolName"));
        headerList.add(makeHeader("类型", "type"));
        headerList.addAll(crv.stream().map(e -> e.getMonth()).collect(Collectors.toSet()).stream().map(e -> makeHeader(e, e)).collect(Collectors.toList()));
        return Result.ok(headerList);
    }

    public YearHeaderV makeHeader(String label, String prop) {
        YearHeaderV y = new YearHeaderV();
        y.setMyLabel(label);
        y.setMyProp(prop);
        return y;
    }
}


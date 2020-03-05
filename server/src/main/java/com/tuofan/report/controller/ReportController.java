package com.tuofan.report.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.core.DynamicBean;
import com.tuofan.core.Result;
import com.tuofan.report.vo.ChargeReportQ;
import com.tuofan.report.vo.ChargeReportV;
import com.tuofan.report.vo.YearHeaderV;
import com.tuofan.report.vo.YearReportV;
import com.tuofan.setting.service.ISysChargeService;
import com.tuofan.student.service.IStudentChargeService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    public Result month(@RequestBody ChargeReportQ chargeReportQ) throws ClassNotFoundException {
        YearReportV yearReportV = new YearReportV();
        yearReportV.setHeader(makeHeader(chargeReportQ));
        yearReportV.setPageRecords(makeRecords(chargeReportQ));
        return Result.ok(yearReportV);
    }

    public DynamicBean makeBean(Set<String> set) throws ClassNotFoundException {
        HashMap propertyMap = new HashMap();
        propertyMap.put("schoolName", Class.forName("java.lang.String"));
        propertyMap.put("type", Class.forName("java.lang.String"));
        for (String s : set) {
            propertyMap.put(s, Class.forName("java.lang.String"));
        }
        DynamicBean bean = new DynamicBean(propertyMap);
        return bean;
    }

    public List<DynamicBean> createBean(List<ChargeReportV> list) throws ClassNotFoundException {
        List<DynamicBean> beanList = Lists.newArrayList();
        for (String s : list.stream().map(e -> e.getSchoolName()).collect(Collectors.toSet())) {
            DynamicBean b = makeBean(list.stream().map(e -> e.getMonth()).collect(Collectors.toSet()));
            b.setValue("schoolName", s);
            b.setValue("type", "完成");
            beanList.add(b);
        }
        return beanList;
    }

    public void makeBeanValue(List<DynamicBean> beanList, ChargeReportV crv) {
        for (DynamicBean bean : beanList) {
            if (bean.getValue("schoolName").equals(crv.getSchoolName())) {
                bean.setValue(crv.getMonth(), crv.getSum().toString());
            }
        }
    }


    public List<YearHeaderV> makeHeader(ChargeReportQ chargeReportQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null) queryWrapper.ge("charge.create_time", chargeReportQ.getBegin());
        List<ChargeReportV> crv = iStudentChargeService.reportMonth(new Page(1, 100), queryWrapper).getRecords();
        List<YearHeaderV> headerList = Lists.newArrayList();
        headerList.add(makeHeader("校区", "schoolName"));
        headerList.add(makeHeader("类型", "type"));
        headerList.addAll(crv.stream().map(e -> e.getMonth()).collect(Collectors.toSet()).stream().map(e -> makeHeader(e, e)).collect(Collectors.toList()));
        return headerList;
    }


    public IPage makeRecords(ChargeReportQ chargeReportQ) throws ClassNotFoundException {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null) queryWrapper.ge("charge.create_time", chargeReportQ.getBegin());
        IPage page = iStudentChargeService.reportMonth(new Page(chargeReportQ.getCurrent(), chargeReportQ.getSize()), queryWrapper);
        List<ChargeReportV> list = page.getRecords();
        List<DynamicBean> beanList = createBean(list);
        for (ChargeReportV crv : list) {
            makeBeanValue(beanList, crv);
        }
        return page.setRecords(beanList.stream().map(e -> e.getObject()).collect(Collectors.toList()));
    }

    public YearHeaderV makeHeader(String label, String prop) {
        YearHeaderV y = new YearHeaderV();
        y.setMyLabel(label);
        y.setMyProp(prop);
        return y;
    }
}


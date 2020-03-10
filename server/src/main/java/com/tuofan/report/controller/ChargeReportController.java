package com.tuofan.report.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.configs.service.ISysConfigsService;
import com.tuofan.core.DynamicBean;
import com.tuofan.core.Result;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.orgination.service.IDingDeptService;
import com.tuofan.report.vo.ChargeReportQ;
import com.tuofan.report.vo.ChargeReportV;
import com.tuofan.report.vo.HeaderV;
import com.tuofan.report.vo.ReportV;
import com.tuofan.setting.service.ISysChargeService;
import com.tuofan.student.service.IStudentChargeService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.*;
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
public class ChargeReportController {

    @Autowired
    private IStudentChargeService iStudentChargeService;

    @Autowired
    private ISysChargeService iSysChargeService;

    @Autowired
    private IDingDeptService iDingDeptService;

    @PostMapping("charge")
    public Result list(@RequestBody ChargeReportQ chargeReportQ) throws ParseException, ClassNotFoundException {
        ReportV yearReportV = new ReportV();
        yearReportV.setHeader(makeHeader(chargeReportQ));
        yearReportV.setPageRecords(makeRecords(chargeReportQ));
        return Result.ok(yearReportV);
    }

    //1
    public DynamicBean makeBean() throws ClassNotFoundException {
        HashMap propertyMap = new HashMap();
        propertyMap.put("schoolName", Class.forName("java.lang.String"));
        for (String s : createType()) {
            propertyMap.put(s, Class.forName("java.lang.String"));
        }
        propertyMap.put("total", Class.forName("java.lang.String"));
        DynamicBean bean = new DynamicBean(propertyMap);
        return bean;
    }

    //2
    public List<DynamicBean> makeBeanList() throws ClassNotFoundException, ParseException {
        List<DynamicBean> beanList = Lists.newArrayList();
        for (String s : iDingDeptService.list().stream().filter(e -> e.getIsSchoolZone()).map(e -> e.getName()).collect(Collectors.toSet())) {
            DynamicBean b = makeBean();
            b.setValue("schoolName", s);
            beanList.add(b);
        }
        DynamicBean b = makeBean();
        b.setValue("schoolName", "总计");
        beanList.add(b);
        return beanList;
    }

    //3
    public void makeBeanValue(List<DynamicBean> beanList, ChargeReportV crv) {
        for (DynamicBean bean : beanList) {
            if (bean.getValue("schoolName").equals(crv.getSchoolName())) {
                bean.setValue(crv.getChargeType(), crv.getSum().toString());
            }
        }
    }

    public void makeBeanValueTotal(List<DynamicBean> beanList, ChargeReportV crv) {
        for (DynamicBean bean : beanList) {
            if (bean.getValue("schoolName").equals("总计")) {
                bean.setValue(crv.getChargeType(), crv.getSum().toString());
            }
        }
    }

    //4
    public List<HeaderV> makeHeader(ChargeReportQ chargeReportQ) {
        List<HeaderV> headerList = Lists.newArrayList();
        headerList.add(makeHeader("校区", "schoolName"));
        createType().stream().forEach(e -> headerList.add(makeHeader(e, e)));
        headerList.add(makeHeader("总计", "total"));
        return headerList;
    }

    //6
    public void makeBeanValueTotal(List<DynamicBean> beanList) {
        List<String> header = createType();
        for (DynamicBean bean : beanList) {
            Float total = 0f;
            for (String prop : header) {
                if (bean.getValue(prop) != null) total += Float.valueOf(bean.getValue(prop).toString());
            }
            bean.setValue("total", total.toString());
        }
    }

    private List<String> createType() {
        return iSysChargeService.list().stream().map(e -> e.getName()).collect(Collectors.toList());
    }

    //5
    public List makeRecords(ChargeReportQ chargeReportQ) throws ClassNotFoundException, ParseException {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null && chargeReportQ.getEnd() != null) queryWrapper.between("charge.create_time", chargeReportQ.getBegin(), chargeReportQ.getEnd());
        List<DynamicBean> beanList = makeBeanList();
        for (ChargeReportV crv : iStudentChargeService.reportChargeType(queryWrapper)) {
            makeBeanValue(beanList, crv);
        }
        for (val rmt : iStudentChargeService.reportChargeTypeTotal(queryWrapper)) {
            makeBeanValueTotal(beanList, rmt);
        }
        makeBeanValueTotal(beanList);
        return beanList.stream().map(e -> e.getObject()).collect(Collectors.toList());
    }

    public HeaderV makeHeader(String label, String prop) {
        HeaderV y = new HeaderV();
        y.setMyLabel(label);
        y.setMyProp(prop.replace("-", "_"));
        return y;
    }
}


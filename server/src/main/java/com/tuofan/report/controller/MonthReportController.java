package com.tuofan.report.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
public class MonthReportController {

    @Autowired
    private ISysConfigsService iSysConfigsService;

    @Autowired
    private IStudentChargeService iStudentChargeService;

    @Autowired
    private IDingDeptService iDingDeptService;

    @PostMapping("month")
    public Result month(@RequestBody ChargeReportQ chargeReportQ) throws ClassNotFoundException, ParseException {
        ReportV yearReportV = new ReportV();
        yearReportV.setHeader(makeHeader(chargeReportQ));
        yearReportV.setPageRecords(makeRecords(chargeReportQ));
        return Result.ok(yearReportV);
    }

    //1
    public DynamicBean makeBean(Date begin) throws ClassNotFoundException {
        HashMap propertyMap = new HashMap();
        propertyMap.put("schoolName", Class.forName("java.lang.String"));
        propertyMap.put("type", Class.forName("java.lang.String"));
        for (String s : createMonthBetween(begin, new Date()).stream().collect(Collectors.toSet())) {
            propertyMap.put(s.replace("-", "_"), Class.forName("java.lang.String"));
        }
        propertyMap.put("total", Class.forName("java.lang.String"));
        DynamicBean bean = new DynamicBean(propertyMap);
        return bean;
    }

    //2
    public List<DynamicBean> makeBeanList() throws ClassNotFoundException, ParseException {
        List<DynamicBean> beanList = Lists.newArrayList();
        val begin = DateTimeUtils.getFormatDate(iSysConfigsService.findByName("app.reportSchoolYearBeginTime").getValue());
        for (String s : iDingDeptService.list().stream().filter(e -> e.getIsSchoolZone()).map(e -> e.getName()).collect(Collectors.toSet())) {
            DynamicBean b = makeBean(begin);
            b.setValue("schoolName", s);
            b.setValue("type", "完成");
            beanList.add(b);
        }
        DynamicBean b = makeBean(begin);
        b.setValue("schoolName", "总计");
        b.setValue("type", "完成");
        beanList.add(b);
        return beanList;
    }

    //3
    public void makeBeanValue(List<DynamicBean> beanList, ChargeReportV crv) {
        for (DynamicBean bean : beanList) {
            if (bean.getValue("schoolName").equals(crv.getSchoolName())) {
                bean.setValue(crv.getMonth().replace("-", "_"), crv.getSum().toString());
            }
        }
    }

    public void makeBeanValueTotal(List<DynamicBean> beanList, ChargeReportV crv) {
        for (DynamicBean bean : beanList) {
            if (bean.getValue("schoolName").equals("总计")) {
                bean.setValue(crv.getMonth().replace("-", "_"), crv.getSum().toString());
            }
        }
    }

    //4
    public List<HeaderV> makeHeader(ChargeReportQ chargeReportQ) {
        List<HeaderV> headerList = Lists.newArrayList();
        headerList.add(makeHeader("校区", "schoolName"));
        headerList.add(makeHeader("类型", "type"));
        val begin = DateTimeUtils.getFormatDate(iSysConfigsService.findByName("app.reportSchoolYearBeginTime").getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 11);
        createMonthBetween(begin, calendar.getTime()).stream().forEach(e -> headerList.add(makeHeader(e, e)));
        headerList.add(makeHeader("总计", "total"));
        return headerList;
    }

    //6
    public void makeBeanValueTotal(List<DynamicBean> beanList) {
        val begin = DateTimeUtils.getFormatDate(iSysConfigsService.findByName("app.reportSchoolYearBeginTime").getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 11);
        List<String> between = createMonthBetween(begin, calendar.getTime());
        for (DynamicBean bean : beanList) {
            Float total = 0f;
            for (String prop : between) {
                prop = prop.replace("-","_");
                if (bean.getValue(prop) != null) total += Float.valueOf(bean.getValue(prop).toString());
            }
            bean.setValue("total", total.toString());
        }
    }

    private static List<String> createMonthBetween(Date begin, Date end) {
        ArrayList<String> result = new ArrayList<String>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        min.setTime(begin);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(end);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(DateTimeUtils.formatDateTime(curr.getTime(), DateTimeUtils.DATE_FORMAT_MONTH));
            curr.add(Calendar.MONTH, 1);
        }
        return result;
    }

    //5
    public List makeRecords(ChargeReportQ chargeReportQ) throws ClassNotFoundException, ParseException {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(chargeReportQ.getSchoolIds())) queryWrapper.in("school.id", chargeReportQ.getSchoolIds());
        if (chargeReportQ.getBegin() != null) queryWrapper.ge("charge.create_time", chargeReportQ.getBegin());
        List<DynamicBean> beanList = makeBeanList();
        for (ChargeReportV crv : iStudentChargeService.reportMonth(queryWrapper)) {
            makeBeanValue(beanList, crv);
        }
        for (val rmt : iStudentChargeService.reportMonthTotal(queryWrapper)) {
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


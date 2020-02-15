package com.tuofan.biz.bill_manage.web;

import com.google.common.collect.Maps;
import com.tuofan.biz.bill_manage.constants.DictTypes;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.service.*;
import com.tuofan.biz.bill_manage.vo.*;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.Subject;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/report")
@Slf4j
public class ReportController {

    @Autowired
    YearReportService yearReportService;

    @Autowired
    PayTypeReportService payTypeReportService;

    @Autowired
    SemesterService semesterService;

    @Autowired
    SubjectStudentReportService subjectStudentReportService;

    @PostMapping("yearReport")
    @LoginRequired
    public Map<String, Object> yearReport(@RequestBody SaleOfYearQuery query) {
        if (query.getStartMonth() == null) {
            throw new BizException("100", "必须提供开始月份");
        }
        if (CollectionUtils.isEmpty(query.getDeptSchoolIds())) {
            throw new BizException("200", "必须提供校区ID");
        }

        Date beginDate = DateTimeUtils.getMonthFirstTime(query.getStartMonth());
        Date endDate = DateTimeUtils.getOffsetMonth(beginDate, 12);
        SaleQuery saleQuery = new SaleQuery();
        saleQuery.setBeginDate(beginDate);
        saleQuery.setEndDate(endDate);
        saleQuery.setDeptSchoolIds(query.getDeptSchoolIds());
        Map<String, Object> map = Maps.newHashMap();
        List<YearSaleVO> list = yearReportService.yearReport(saleQuery);
        map.put("content", list);
        map.put("title", yearReportService.yearReportTitle(query));
        return map;
    }

    @PostMapping("payTypeReport")
    @LoginRequired
    public Map<String, Object> payTypeReport(@RequestBody SaleQuery query) {
        if (query.getBeginDate() == null) {
            throw new BizException("100", "必须提供开始时间");
        }
        if (query.getEndDate() == null) {
            throw new BizException("200", "必须提供结束时间");
        }
        if (CollectionUtils.isEmpty(query.getDeptSchoolIds())) {
            throw new BizException("200", "必须提供校区ID");
        }
        Date beginDate = DateTimeUtils.getDateFirstTime(query.getBeginDate());
        Date endDate = DateTimeUtils.getDateEndTime(query.getEndDate());
        query.setBeginDate(beginDate);
        query.setEndDate(endDate);
        log.info("支付类型报表查询，参数={}", query);
        Map<String, Object> map = Maps.newHashMap();
        List<PayTypeSaleVO> list = payTypeReportService.payTypeReport(query);
        map.put("content", list);
        map.put("dynamicTitle", payTypeReportService.listDynamicColumn());
        return map;
    }

    @PostMapping("subjectStudentReport")
    @LoginRequired
    public Map<String, Object> subjectStudentReport(@RequestBody SubjectStudentQuery query) {
        if (CollectionUtils.isEmpty(query.getDeptSchoolIds())) {
            throw new BizException("200", "必须提供校区ID");
        }
        if (query.getSemesterId() == null || query.getSemesterId() <= 0) {
            SysDict curSemester = semesterService.getCurrentSemester();
            if (curSemester == null) {
                throw new BizException("200", "没有指定学期，且系统无默认学期");
            }
            query.setSemesterId(curSemester.getId());
        }
        log.info("科目学生-报表查询，参数={}", query);
        Map<String, Object> map = Maps.newHashMap();
        map.put("content", subjectStudentReportService.subjectStudentReport(query));
        map.put("dynamicTitle", subjectStudentReportService.listDynamicColumn());
        return map;
    }
}

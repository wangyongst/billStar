package com.tuofan.student.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.core.SearchQ;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.service.IStudentChargeService;
import com.tuofan.student.vo.StudentChargeQ;
import com.tuofan.student.vo.StudentCourseQ;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 学生-课程记录 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@RestController
@RequestMapping("/bill/student/charge")
public class StudentChargeController {

    @Autowired
    private IStudentChargeService iStudentChargeService;

    @PostMapping("pageV")
    public Result pageV(@RequestBody StudentChargeQ studentChargeQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentChargeQ.getSchoolIds())) queryWrapper.in("school.id", studentChargeQ.getSchoolIds());
        if (!CollectionUtils.isEmpty(studentChargeQ.getTypes())) queryWrapper.in("charge.type", studentChargeQ.getTypes());
        if (studentChargeQ.getTime() != null) {
            if (studentChargeQ.getTime() == 1) queryWrapper.between("charge.create_time", today(), new Date());
            if (studentChargeQ.getTime() == 3) queryWrapper.between("charge.create_time", month(), new Date());
            if (studentChargeQ.getTime() == 5) queryWrapper.between("charge.create_time", lastMonth(), month());
        }
        queryWrapper.orderByDesc("charge.create_time");
        return Result.ok(iStudentChargeService.pageV(new Page(studentChargeQ.getCurrent(), studentChargeQ.getSize()), queryWrapper));
    }

    @GetMapping("list/{studentId}")
    public Result list(@PathVariable Integer studentId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("studentCharge.student_id", studentId);
        return Result.ok(iStudentChargeService.listStudentChargeV(queryWrapper));
    }

    //当天开始
    private Date today() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.getTimeInMillis();
        return new Date(calendar.getTimeInMillis());
    }

    //当月开始
    private Date month() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

    //上月开始
    private Date lastMonth() {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Date(calendar.getTimeInMillis());
    }

}


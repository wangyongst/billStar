package com.tuofan.student.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.core.SearchQ;
import com.tuofan.orgination.vo.TeacherQ;
import com.tuofan.student.service.IStudentCourseService;
import com.tuofan.student.vo.StudentCourseQ;
import com.tuofan.student.vo.StudentMainQ;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/bill/student/course")
public class StudentCourseController {

    @Autowired
    private IStudentCourseService iStudentCourseService;

    //到期
    @PostMapping("pageExpire")
    public Result pageExpire(@RequestBody StudentCourseQ studentCourseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentCourseQ.getSchoolIds())) queryWrapper.in("school.id", studentCourseQ.getSchoolIds());
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherName())) queryWrapper.in("teacher_name", studentCourseQ.getTeacherName());
        if (studentCourseQ.getRadio() == null) queryWrapper.le("studentCourse.expire_time", new Date());
        if (studentCourseQ.getDays() != null && studentCourseQ.getDays() != 0 && studentCourseQ.getRadio() == 1) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DAY_OF_MONTH, studentCourseQ.getDays());
            Date end = c.getTime();
            queryWrapper.between("studentCourse.expire_time", new Date(), end);
        }
        if (studentCourseQ.getBefore() != null && studentCourseQ.getRadio() == 2) queryWrapper.between("studentCourse.expire_time", new Date(), studentCourseQ.getBefore());
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }

    //超期
    @PostMapping("pageOverTime")
    public Result pageOverTime(@RequestBody StudentCourseQ studentCourseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentCourseQ.getSchoolIds())) queryWrapper.in("school.id", studentCourseQ.getSchoolIds());
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherName())) queryWrapper.in("teacher_name", studentCourseQ.getTeacherName());
        queryWrapper.le("studentCourse.expire_time", new Date());
        queryWrapper.eq("student.type",0);
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }

    //流失
    @PostMapping("pageLost")
    public Result pageLost(@RequestBody StudentCourseQ studentCourseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentCourseQ.getSchoolIds())) queryWrapper.in("school.id", studentCourseQ.getSchoolIds());
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherName())) queryWrapper.in("teacher_name", studentCourseQ.getTeacherName());
        if (studentCourseQ.getMonth() != null) queryWrapper.ge("student.lost_time", lastMonth(studentCourseQ.getMonth()));
        queryWrapper.eq("student.type", 2);
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }

    //退费
    @PostMapping("pageTui")
    public Result pageTui(@RequestBody StudentCourseQ studentCourseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentCourseQ.getSchoolIds())) queryWrapper.in("school.id", studentCourseQ.getSchoolIds());
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherName())) queryWrapper.in("teacher_name", studentCourseQ.getTeacherName());
        queryWrapper.eq("student.type", 4);
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }


    @PostMapping("pageArrears")
    public Result arrearsPage(@RequestBody StudentCourseQ studentCourseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("student.type", 1);
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherName())) queryWrapper.in("teacher_name", studentCourseQ.getTeacherName());
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }

    @PostMapping("pageXu")
    public Result pageXu(@RequestBody StudentCourseQ studentCourseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("student.type", 0);
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }


    //前几月开始
    private Date lastMonth(int before) {
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        calendar.add(Calendar.MONTH, -before);
        return calendar.getTime();
    }
}


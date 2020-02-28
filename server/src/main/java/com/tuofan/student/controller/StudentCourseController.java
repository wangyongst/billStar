package com.tuofan.student.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.orgination.vo.TeacherQ;
import com.tuofan.student.service.IStudentCourseService;
import com.tuofan.student.vo.StudentCourseQ;
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
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherIds())) queryWrapper.in("teacher.id", studentCourseQ.getTeacherIds());
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
        if (!CollectionUtils.isEmpty(studentCourseQ.getTeacherIds())) queryWrapper.in("teacher.id", studentCourseQ.getTeacherIds());
        queryWrapper.le("studentCourse.expire_time", new Date());
        return Result.ok(iStudentCourseService.pageV(new Page(studentCourseQ.getCurrent(), studentCourseQ.getSize()), queryWrapper));
    }
}


package com.tuofan.student.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.core.SearchQ;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.service.IStudentChargeService;
import com.tuofan.student.vo.StudentCourseQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("pageOverTime")
    public Result pageOverTime(@RequestBody SearchQ searchQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(searchQ.getSchoolIds())) queryWrapper.in("school.id", searchQ.getSchoolIds());
        if (!CollectionUtils.isEmpty(searchQ.getSubjectIds())) queryWrapper.in("subject.id", searchQ.getSubjectIds());
        if (!CollectionUtils.isEmpty(searchQ.getClassIds())) queryWrapper.in("class.id", searchQ.getClassIds());
        queryWrapper.le("studentCourse.expire_time",new Date());
        return Result.ok(iStudentChargeService.pageV(new Page(searchQ.getCurrent(), searchQ.getSize()), queryWrapper));
    }

}


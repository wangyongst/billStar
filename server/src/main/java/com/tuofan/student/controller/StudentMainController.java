package com.tuofan.student.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.course.vo.CourseP;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.service.IStudentChargeService;
import com.tuofan.student.service.IStudentCourseService;
import com.tuofan.student.service.IStudentMainService;
import com.tuofan.student.vo.StudentP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学生信息 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@RestController
@RequestMapping("/bill/student/main")
public class StudentMainController {

    @Autowired
    private IStudentCourseService iStudentCourseService;

    @Autowired
    private IStudentMainService iStudentMainService;

    @Autowired
    private IStudentChargeService iStudentChargeService;


    @PostMapping("create")
    public Result create(@RequestHeader(LoginConstants.USER_ID) String userId, @RequestBody StudentP studentP) {
        if (studentP.getArrears() != null && studentP.getArrears().intValue() != 0) studentP.setType(1);
        iStudentMainService.save(studentP);
        if (studentP.getCharge().getAmount() != null) {
            StudentCharge sc = studentP.getCharge();
            sc.setStudentId(studentP.getId());
            sc.setType(1);
            sc.setCreateBy(userId);
            sc.setCreateTime(new Date());
            iStudentChargeService.save(sc);
        }
        List<StudentCourse> studentCourse = Lists.newArrayList();
        for (StudentCourse sc : studentP.getCourseList()) {
            if (sc.getCourseId() != null && sc.getCourseId() != 0) {
                sc.setStudentId(studentP.getId());
                studentCourse.add(sc);
            }
        }
        if (!CollectionUtils.isEmpty(studentCourse)) iStudentCourseService.saveBatch(studentCourse);
        return Result.ok("保存成功");
    }

    @GetMapping("arrears")
    public Result arrears(Integer studentId) {
        StudentMain studentMain = iStudentMainService.getById(studentId);
        studentMain.setType(1);
        iStudentMainService.updateById(studentMain);
        return Result.ok();
    }


    @GetMapping("lose")
    public Result lose(Integer studentId) {
        StudentMain studentMain = iStudentMainService.getById(studentId);
        studentMain.setType(2);
        studentMain.setLostTime(new Date());
        iStudentMainService.updateById(studentMain);
        return Result.ok();
    }
}


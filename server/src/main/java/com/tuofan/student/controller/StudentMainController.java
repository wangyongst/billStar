package com.tuofan.student.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.core.SearchQ;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.entity.CourseTime;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.course.service.ICourseTimeService;
import com.tuofan.course.vo.CourseP;
import com.tuofan.orgination.entity.DingUser;
import com.tuofan.orgination.service.IDingUserService;
import com.tuofan.setting.entity.SysClass;
import com.tuofan.setting.service.ISysClassService;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.service.IStudentChargeService;
import com.tuofan.student.service.IStudentCourseService;
import com.tuofan.student.service.IStudentMainService;
import com.tuofan.student.vo.StudentCourseQ;
import com.tuofan.student.vo.StudentMainQ;
import com.tuofan.student.vo.StudentP;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.couchbase.CouchbaseReactiveDataAutoConfiguration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ICourseMainService iCourseMainService;

    @Autowired
    private IStudentChargeService iStudentChargeService;

    @Autowired
    private ISysClassService iSysClassService;

    @Autowired
    private ICourseTimeService iCourseTimeService;

    @Autowired
    private IDingUserService iDingUserService;


    @PostMapping("create")
    public Result create(@RequestHeader(LoginConstants.USER_ID) String userId, @RequestBody StudentP studentP) {
        if (studentP.getArrears() != null && studentP.getArrears().intValue() != 0) studentP.setType(1);
        studentP.setCreateBy(userId);
        studentP.setCreateTime(new Date());
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
                CourseMain course = iCourseMainService.getById(sc.getCourseId());
                sc.setClassNo(course.getClassNo());
                sc.setClassRoom(course.getClassRoom());
                sc.setTeacherName(iDingUserService.getById(course.getTeacherId()).getName());
                SysClass sysClass = iSysClassService.getById(course.getClassId());
                sc.setClassName(sysClass.getName());
                StringBuffer stringBuffer = new StringBuffer();
                for (CourseTime ct : iCourseTimeService.listByIds(Arrays.asList(course.getTimeIds().split(",")))) {
                    stringBuffer.append(ct.getDay() + ":" + DateTimeUtils.formatTime(ct.getBegin()) + "-" + DateTimeUtils.formatTime(ct.getEnd()));
                    stringBuffer.append("\n");
                }
                sc.setCourseTime(stringBuffer.toString());
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

    @PostMapping("updateArrears")
    public Result updateArrears(@RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getId());
        studentMain.setArrears(studentP.getArrears());
        iStudentMainService.updateById(studentMain);
        return Result.ok("修改成功");
    }

    @GetMapping("lose")
    public Result lose(Integer studentId) {
        StudentMain studentMain = iStudentMainService.getById(studentId);
        studentMain.setType(2);
        studentMain.setLostTime(new Date());
        iStudentMainService.updateById(studentMain);
        return Result.ok();
    }

    @PostMapping("pageV")
    public Result pageV(@RequestBody StudentMainQ studentMainQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentMainQ.getSchoolIds())) queryWrapper.in("school.id", studentMainQ.getTeacherName());
        if (StringUtils.isNotBlank(studentMainQ.getNameLike())) queryWrapper.like("student.name", studentMainQ.getNameLike());
        if (StringUtils.isNotBlank(studentMainQ.getMobileLike())) queryWrapper.like("student.mobile", studentMainQ.getMobileLike());
        if (studentMainQ.getIsarrears() != null && studentMainQ.getIsarrears() == 1) queryWrapper.like("student.arrears", 1);
        return Result.ok(iStudentMainService.pageV(new Page(studentMainQ.getCurrent(), studentMainQ.getSize()), queryWrapper));
    }
}


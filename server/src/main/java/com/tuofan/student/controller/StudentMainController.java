package com.tuofan.student.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.core.CheckUtils;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.MultiResult;
import com.tuofan.core.Result;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.course.service.ICourseTimeService;
import com.tuofan.orgination.service.IDingUserService;
import com.tuofan.setting.service.ISysMyClassService;
import com.tuofan.setting.service.ISysMySchoolService;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.service.IStudentChargeService;
import com.tuofan.student.service.IStudentCourseService;
import com.tuofan.student.service.IStudentMainService;
import com.tuofan.student.vo.StudentMainQ;
import com.tuofan.student.vo.StudentP;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
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
    private ISysMyClassService iSysMyClassService;

    @Autowired
    private ISysMySchoolService iSysMySchoolService;

    @Autowired
    private IStudentChargeService iStudentChargeService;

    @Autowired
    private ICourseMainService iCourseMainService;

    @Autowired
    private ICourseTimeService iCourseTimeService;

    @Autowired
    private IDingUserService iDingUserService;


    @PostMapping("create")
    public Result create(@RequestHeader(LoginConstants.USER_ID) String userId, @RequestBody StudentP studentP) {
        if (studentP.getArrears() != null && studentP.getArrears().intValue() != 0) studentP.setType(1);
        studentP.setCreateBy(userId);
        studentP.setCreateTime(new Date());
        if (CheckUtils.isNotZero(studentP.getMyclassId())) studentP.setMyclass(iSysMyClassService.getById(studentP.getMyclassId()).getName());
        if (CheckUtils.isNotZero(studentP.getMyschoolId())) studentP.setMyschool(iSysMySchoolService.getById(studentP.getMyschoolId()).getName());
        iStudentMainService.save(studentP);
        if (studentP.getCharge().getAmount() != null && studentP.getCharge().getChargeId() != null) {
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
        if (!CollectionUtils.isEmpty(studentCourse)) {
            iStudentCourseService.saveBatch(studentCourse);
            for (val sc : studentCourse) {
                CourseMain courseMain = iCourseMainService.getById(sc.getCourseId());
                iCourseMainService.addStudentNum(courseMain);
            }
        }
        return Result.ok("保存成功");
    }

    //nameLike
    @GetMapping("listByNameLike/{nameLike}")
    public Result list(@PathVariable String nameLike) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like("name", nameLike);
        return Result.ok(iStudentMainService.list(queryWrapper));
    }


    //欠费
    @PostMapping("pageArrear")
    public Result pageArrear(@RequestBody StudentMainQ studentMainQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("student.type", 1);
        if (!CollectionUtils.isEmpty(studentMainQ.getSchoolIds())) queryWrapper.in("school.id", studentMainQ.getSchoolIds());
        return Result.ok(new MultiResult(iStudentMainService.pageArrear(new Page(studentMainQ.getCurrent(), studentMainQ.getSize()), queryWrapper), iStudentMainService.sumArrear()));
    }

    //设为欠费
    @GetMapping("arrears")
    public Result arrears(Integer studentId) {
        StudentMain studentMain = iStudentMainService.getById(studentId);
        studentMain.setType(1);
        iStudentMainService.updateById(studentMain);
        return Result.ok();
    }

    //欠费金额
    @PostMapping("updateArrears")
    public Result updateArrears(@RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getId());
        studentMain.setArrears(studentP.getArrears());
        iStudentMainService.updateById(studentMain);
        return Result.ok("修改成功");
    }

    //补费
    @PostMapping("buCharge")
    public Result charge(@RequestHeader(LoginConstants.USER_ID) String userId, @RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getStudentId());
        StudentCharge charge = new StudentCharge();
        charge.setChargeId(studentP.getChargeId());
        charge.setType(3);
        charge.setAmount(studentP.getAmount());
        charge.setStudentId(studentMain.getId());
        charge.setCreateBy(userId);
        charge.setCreateTime(new Date());
        iStudentChargeService.save(charge);
        if (charge.getAmount() < studentMain.getArrears()) {
            studentMain.setArrears(studentMain.getArrears() - charge.getAmount());
        } else {
            studentMain.setType(0);
            studentMain.setArrears(0f);
        }
        iStudentMainService.updateById(studentMain);
        return Result.ok("补费成功");
    }

    //流失
    @GetMapping("lose")
    public Result lose(Integer studentId) {
        StudentMain studentMain = iStudentMainService.getById(studentId);
        studentMain.setType(2);
        studentMain.setLostTime(new Date());
        iStudentMainService.updateById(studentMain);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("student_id", studentMain.getId());
        List<StudentCourse> studentCourses = iStudentCourseService.list(queryWrapper);
        for (val sc : studentCourses) {
            CourseMain courseMain = iCourseMainService.getById(sc.getCourseId());
            iCourseMainService.reduceStudentNum(courseMain);
        }
        iStudentCourseService.remove(queryWrapper);
        return Result.ok();
    }

    @PostMapping("pageV")
    public Result pageV(@RequestBody StudentMainQ studentMainQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentMainQ.getSchoolIds())) queryWrapper.in("school.id", studentMainQ.getSchoolIds());
        if (StringUtils.isNotBlank(studentMainQ.getNameLike())) queryWrapper.like("student.name", studentMainQ.getNameLike());
        if (StringUtils.isNotBlank(studentMainQ.getMobileLike())) queryWrapper.like("student.mobile", studentMainQ.getMobileLike());
        if (studentMainQ.getIsArrears() != null && studentMainQ.getIsArrears() == 1) queryWrapper.eq("student.type", 0);
        if (studentMainQ.getIsArrears() != null && studentMainQ.getIsArrears() == 0) queryWrapper.ne("student.type", 1);
        return Result.ok(iStudentMainService.pageV(new Page(studentMainQ.getCurrent(), studentMainQ.getSize()), queryWrapper));
    }

    //续费
    @PostMapping("pageXiu")
    public Result pageXiu(@RequestBody StudentMainQ studentMainQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentMainQ.getSchoolIds())) queryWrapper.in("school.id", studentMainQ.getSchoolIds());
        if (StringUtils.isNotBlank(studentMainQ.getNameLike())) queryWrapper.like("student.name", studentMainQ.getNameLike());
        queryWrapper.eq("student.type", 0);
        return Result.ok(iStudentMainService.pageV(new Page(studentMainQ.getCurrent(), studentMainQ.getSize()), queryWrapper));
    }

    //复学
    @PostMapping("pageFu")
    public Result pageFu(@RequestBody StudentMainQ studentMainQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(studentMainQ.getSchoolIds())) queryWrapper.in("school.id", studentMainQ.getSchoolIds());
        if (StringUtils.isNotBlank(studentMainQ.getNameLike())) queryWrapper.like("student.name", studentMainQ.getNameLike());
        queryWrapper.eq("student.type", 3);
        return Result.ok(iStudentMainService.pageV(new Page(studentMainQ.getCurrent(), studentMainQ.getSize()), queryWrapper));
    }

    @PostMapping("tui")
    public Result tuifei(@RequestHeader(LoginConstants.USER_ID) String userId, @RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getStudentId());
        StudentCharge charge = new StudentCharge();
        charge.setChargeId(studentP.getChargeId());
        charge.setType(4);
        charge.setAmount(-studentP.getAmount());
        charge.setStudentId(studentMain.getId());
        charge.setCreateBy(userId);
        charge.setCreateTime(new Date());
        iStudentChargeService.save(charge);
        return Result.ok("退费成功");
    }

    @PostMapping("xu")
    public Result xufei(@RequestHeader(LoginConstants.USER_ID) String userId, @RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getStudentId());
        StudentCharge charge = new StudentCharge();
        charge.setChargeId(studentP.getChargeId());
        charge.setType(2);
        charge.setAmount(studentP.getAmount());
        charge.setStudentId(studentMain.getId());
        charge.setCreateBy(userId);
        charge.setCreateTime(new Date());
        iStudentChargeService.save(charge);
        return Result.ok("续费成功");
    }


    @PostMapping("xiu")
    public Result xiu(@RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getStudentId());
        studentMain.setType(3);
        studentMain.setFuTime(studentP.getFuTime());
        iStudentMainService.updateById(studentMain);
        return Result.ok();
    }

    @PostMapping("fu")
    public Result fu(@RequestBody StudentP studentP) {
        StudentMain studentMain = iStudentMainService.getById(studentP.getStudentId());
        studentMain.setType(0);
        studentMain.setFuTime(null);
        iStudentMainService.updateById(studentMain);
        return Result.ok();
    }
}


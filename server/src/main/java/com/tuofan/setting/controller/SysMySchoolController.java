package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.entity.SysClassRoom;
import com.tuofan.setting.entity.SysMySchool;
import com.tuofan.setting.service.ISysClassNoService;
import com.tuofan.setting.service.ISysMySchoolService;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.service.IStudentMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-03-04
 */
@RestController
@RequestMapping("/bill/sys/my/school")
public class SysMySchoolController {


    @Autowired
    private ISysMySchoolService iSysMySchoolService;

    @Autowired
    private IStudentMainService iStudentMainService;


    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysMySchoolService.listV());
    }


    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysMySchool sysMySchool) {
        sysMySchool.setCreateBy(userid);
        sysMySchool.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysMySchool.getName());
        SysMySchool saved = iSysMySchoolService.getOne(queryWrapper);
        if (saved != null) return Result.error("学生学校不能重复");
        iSysMySchoolService.saveOrUpdate(sysMySchool);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysMySchool sysMySchool) {
        sysMySchool.setCreateBy(userid);
        sysMySchool.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysMySchool.getName());
        SysMySchool saved = iSysMySchoolService.getOne(queryWrapper);
        if (saved != null && saved.getId() != sysMySchool.getId()) return Result.error("学生学校不能重复");
        iSysMySchoolService.saveOrUpdate(sysMySchool);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("myschool_id", saved.getId());
        StudentMain studentMain = new StudentMain();
        studentMain.setMyschoolId(saved.getId());
        studentMain.setMyschool(saved.getName());
        iStudentMainService.update(studentMain, wrapper);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("myschool_id", id);
        if (iStudentMainService.list(query).size() > 0) return Result.error("有学生，不能删除");
        iSysMySchoolService.removeById(id);
        return Result.ok();
    }
}


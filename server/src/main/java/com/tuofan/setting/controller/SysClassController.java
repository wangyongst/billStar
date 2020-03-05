package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.setting.entity.SysClass;
import com.tuofan.setting.service.ISysClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
@RestController
@RequestMapping("/bill/sys/class")
public class SysClassController {

    @Autowired
    private ISysClassService iSysClassService;

    @Autowired
    private ICourseMainService iCourseMainService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysClassService.listV());
    }


    @PostMapping("listClass")
    public Result ListClass(@RequestBody SysClass sysClass) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (sysClass.getSubjectId() != null && sysClass.getSubjectId() != 0) queryWrapper.eq("subject_id", sysClass.getSubjectId());
        return Result.ok(iSysClassService.list(queryWrapper));
    }

    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClass sysClass) {
        sysClass.setCreateBy(userid);
        sysClass.setCreateTime(new Date());
        iSysClassService.saveOrUpdate(sysClass);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClass sysClass) {
        sysClass.setCreateBy(userid);
        sysClass.setCreateTime(new Date());
        iSysClassService.saveOrUpdate(sysClass);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("class_id", sysClass.getId());
        CourseMain courseMain = new CourseMain();
        courseMain.setClassId(sysClass.getId());
        courseMain.setClassName(sysClass.getName());
        iCourseMainService.update(courseMain, wrapper);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("class_id", id);
        if (iCourseMainService.list(query).size() > 0) return Result.error("有课程，不能删除");
        iSysClassService.removeById(id);
        return Result.ok();
    }


}


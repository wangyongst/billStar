package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.entity.SysClassRoom;
import com.tuofan.setting.entity.SysMyClass;
import com.tuofan.setting.service.ISysClassNoService;
import com.tuofan.setting.service.ISysMyClassService;
import com.tuofan.student.entity.StudentMain;
import com.tuofan.student.service.IStudentMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-03-04
 */
@RestController
@RequestMapping("/bill/sys/my/class")
public class SysMyClassController {


    @Autowired
    private ISysMyClassService iSysMyClassService;

    @Autowired
    private IStudentMainService iStudentMainService;


    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysMyClassService.listV());
    }


    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysMyClass sysMyClass) {
        sysMyClass.setCreateBy(userid);
        sysMyClass.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysMyClass.getName());
        SysMyClass saved = iSysMyClassService.getOne(queryWrapper);
        if (saved != null) return Result.error("学生班级不能重复");
        iSysMyClassService.saveOrUpdate(sysMyClass);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysMyClass sysMyClass) {
        sysMyClass.setCreateBy(userid);
        sysMyClass.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysMyClass.getName());
        SysMyClass saved = iSysMyClassService.getOne(queryWrapper);
        if (saved != null && saved.getId() != sysMyClass.getId()) return Result.error("学生班级不能重复");
        iSysMyClassService.saveOrUpdate(sysMyClass);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("myclass_id", saved.getId());
        StudentMain studentMain = new StudentMain();
        studentMain.setMyclassId(saved.getId());
        studentMain.setMyclass(saved.getName());
        iStudentMainService.update(studentMain, wrapper);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("myclass_id", id);
        if (iStudentMainService.list(query).size() > 0) return Result.error("有学生，不能删除");
        iSysMyClassService.removeById(id);
        return Result.ok();
    }

}


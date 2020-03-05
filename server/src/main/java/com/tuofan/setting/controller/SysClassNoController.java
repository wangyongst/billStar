package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.service.ISysClassNoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-03-01
 */
@RestController
@RequestMapping("/bill/sys/class/no")
public class SysClassNoController {

    @Autowired
    private ISysClassNoService iSysClassNoService;

    @Autowired
    private ICourseMainService iCourseMainService;


    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysClassNoService.listV());
    }


    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassNo sysClassNo) {
        sysClassNo.setCreateBy(userid);
        sysClassNo.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysClassNo.getName());
        SysClassNo saved = iSysClassNoService.getOne(queryWrapper);
        if (saved != null) return Result.error("班别不能重复");
        iSysClassNoService.saveOrUpdate(sysClassNo);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassNo sysClassNo) {
        sysClassNo.setCreateBy(userid);
        sysClassNo.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysClassNo.getName());
        SysClassNo saved = iSysClassNoService.getOne(queryWrapper);
        if (saved != null && saved.getId() != sysClassNo.getId()) return Result.error("班别不能重复");
        iSysClassNoService.saveOrUpdate(sysClassNo);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("class_no_id", id);
        if (iCourseMainService.list(query).size() > 0) return Result.error("有课程，不能删除");
        iSysClassNoService.removeById(id);
        return Result.ok();
    }
}


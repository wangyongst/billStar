package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.entity.SysClassRoom;
import com.tuofan.setting.entity.SysMySchool;
import com.tuofan.setting.service.ISysClassNoService;
import com.tuofan.setting.service.ISysMySchoolService;
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
@RequestMapping("/bill/sys/my/school")
public class SysMySchoolController {


    @Autowired
    private ISysMySchoolService iSysMySchoolService;


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
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysMySchoolService.removeById(id);
        return Result.ok();
    }
}


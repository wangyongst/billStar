package com.tuofan.setting.controller;


import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
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

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysClassService.listV());
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
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysClassService.removeById(id);
        return Result.ok();
    }


}


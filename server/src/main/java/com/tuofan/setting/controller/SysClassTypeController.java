package com.tuofan.setting.controller;


import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.setting.entity.SysClassType;
import com.tuofan.setting.entity.SysSubject;
import com.tuofan.setting.service.ISysClassTypeService;
import com.tuofan.setting.service.ISysSubjectService;
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
@RequestMapping("/bill/sys/classType")
public class SysClassTypeController {

    @Autowired
    private ISysClassTypeService iSysClassTypeService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysClassTypeService.listV());
    }

    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassType sysClassType) {
        sysClassType.setCreateBy(userid);
        sysClassType.setCreateTime(new Date());
        iSysClassTypeService.saveOrUpdate(sysClassType);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassType sysClassType) {
        sysClassType.setCreateBy(userid);
        sysClassType.setCreateTime(new Date());
        iSysClassTypeService.saveOrUpdate(sysClassType);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysClassTypeService.removeById(id);
        return Result.ok();
    }


}


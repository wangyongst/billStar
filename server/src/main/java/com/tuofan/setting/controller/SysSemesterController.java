package com.tuofan.setting.controller;


import com.tuofan.setting.entity.SysSemester;
import com.tuofan.setting.service.ISysSemesterService;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-02-22
 */
@RestController
@RequestMapping("/bill/sys/semester")
public class SysSemesterController {

    @Autowired
    private ISysSemesterService iSysSemesterService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysSemesterService.listV());
    }

    @PostMapping("saveOrUpdate")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysSemester sysSemester) {
        if (sysSemester.getId() == null || sysSemester.getId() == 0) {
            sysSemester.setCreateBy(userid);
            sysSemester.setCreateTime(new Date());
            iSysSemesterService.saveOrUpdate(sysSemester);
            return Result.ok();
        }
        SysSemester saved = iSysSemesterService.getById(sysSemester.getId());
        saved.setName(sysSemester.getName());
        saved.setCreateBy(userid);
        saved.setCreateTime(new Date());
        iSysSemesterService.saveOrUpdate(saved);
        return Result.ok();
    }

    @PostMapping("setDefault")
    public Result setDefault(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysSemester sysSemester) {
        iSysSemesterService.allNotDefault();
        SysSemester saved = iSysSemesterService.getById(sysSemester.getId());
        saved.setIsDefault(true);
        saved.setCreateBy(userid);
        saved.setCreateTime(new Date());
        iSysSemesterService.saveOrUpdate(saved);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysSemesterService.removeById(id);
        return Result.ok();
    }

}


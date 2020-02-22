package com.tuofan.setting.controller;


import com.tuofan.setting.entity.SysSubject;
import com.tuofan.setting.service.ISysSubjectService;
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
@RequestMapping("/bill/sys/subject")
public class SysSubjectController {

    @Autowired
    private ISysSubjectService iSysSubjectService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysSubjectService.listV());
    }

    @PostMapping("saveOrUpdate")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysSubject sysSubject) {
        sysSubject.setCreateBy(userid);
        sysSubject.setCreateTime(new Date());
        iSysSubjectService.saveOrUpdate(sysSubject);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysSubjectService.removeById(id);
        return Result.ok();
    }

}


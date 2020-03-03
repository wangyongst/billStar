package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
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


    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysClassNoService.listV());
    }


    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassNo sysClassNo) {
        sysClassNo.setCreateBy(userid);
        sysClassNo.setCreateTime(new Date());
        iSysClassNoService.saveOrUpdate(sysClassNo);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassNo sysClassNo) {
        sysClassNo.setCreateBy(userid);
        sysClassNo.setCreateTime(new Date());
        iSysClassNoService.saveOrUpdate(sysClassNo);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysClassNoService.removeById(id);
        return Result.ok();
    }
}


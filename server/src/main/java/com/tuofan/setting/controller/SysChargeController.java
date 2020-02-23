package com.tuofan.setting.controller;


import com.tuofan.setting.entity.SysCharge;
import com.tuofan.setting.service.ISysChargeService;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/bill/sys/charge")
public class SysChargeController {

    @Autowired
    private ISysChargeService iSysChargeService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysChargeService.listV());
    }

    @PostMapping("saveOrUpdate")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysCharge charge) {
        charge.setCreateBy(userid);
        charge.setCreateTime(new Date());
        iSysChargeService.saveOrUpdate(charge);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysChargeService.removeById(id);
        return Result.ok();
    }

}


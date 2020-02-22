package com.tuofan.setting.controller;


import com.tuofan.setting.entity.SysChargeType;
import com.tuofan.setting.service.ISysChargeTypeService;
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
@RequestMapping("/bill/sys/chargeType")
public class SysChargeTypeController {

    @Autowired
    private ISysChargeTypeService iSysChargeTypeService;

    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysChargeTypeService.listV());
    }

    @PostMapping("saveOrUpdate")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysChargeType chargeType) {
        chargeType.setCreateBy(userid);
        chargeType.setCreateTime(new Date());
        iSysChargeTypeService.saveOrUpdate(chargeType);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysChargeTypeService.removeById(id);
        return Result.ok();
    }

}


package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.setting.entity.SysCharge;
import com.tuofan.setting.service.ISysChargeService;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.service.IStudentChargeService;
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

    @Autowired
    private IStudentChargeService iStudentChargeService;

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
        QueryWrapper query = new QueryWrapper();
        query.eq("charge_id",id);
        if(iStudentChargeService.list(query).size()>0) return Result.error("有收费记录，不能删除");
        iSysChargeService.removeById(id);
        return Result.ok();
    }

}


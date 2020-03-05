package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.setting.entity.SysClassNo;
import com.tuofan.setting.entity.SysClassRoom;
import com.tuofan.setting.service.ISysClassRoomService;
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
@RequestMapping("/bill/sys/class/room")
public class SysClassRoomController {

    @Autowired
    private ISysClassRoomService iSysClassRoomService;


    @GetMapping("list")
    public Result list() {
        return Result.ok(iSysClassRoomService.listV());
    }


    @PostMapping("save")
    public Result save(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassRoom sysClassRoom) {
        sysClassRoom.setCreateBy(userid);
        sysClassRoom.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysClassRoom.getName());
        SysClassRoom saved = iSysClassRoomService.getOne(queryWrapper);
        if (saved != null) return Result.error("教室不能重复");
        iSysClassRoomService.saveOrUpdate(sysClassRoom);
        return Result.ok();
    }

    @PostMapping("update")
    public Result update(@RequestHeader(LoginConstants.USER_ID) String userid, @RequestBody SysClassRoom sysClassRoom) {
        sysClassRoom.setCreateBy(userid);
        sysClassRoom.setCreateTime(new Date());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("name", sysClassRoom.getName());
        SysClassRoom saved = iSysClassRoomService.getOne(queryWrapper);
        if (saved != null && saved.getId() != sysClassRoom.getId()) return Result.error("教室不能重复");
        iSysClassRoomService.saveOrUpdate(sysClassRoom);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        iSysClassRoomService.removeById(id);
        return Result.ok();
    }

}


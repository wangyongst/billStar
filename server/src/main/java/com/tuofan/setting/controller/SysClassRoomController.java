package com.tuofan.setting.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.tuofan.core.LoginConstants;
import com.tuofan.core.Result;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.service.ICourseMainService;
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

    @Autowired
    private ICourseMainService iCourseMainService;


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
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("class_room_id", sysClassRoom.getId());
        CourseMain courseMain = new CourseMain();
        courseMain.setClassId(sysClassRoom.getId());
        courseMain.setClassRoom(sysClassRoom.getName());
        iCourseMainService.update(courseMain, wrapper);
        return Result.ok();
    }

    @PostMapping("delete")
    public Result save(@RequestParam Integer id) {
        QueryWrapper query = new QueryWrapper();
        query.eq("class_id", id);
        if (iCourseMainService.list(query).size() > 0) return Result.error("有课程，不能删除");
        iSysClassRoomService.removeById(id);
        return Result.ok();
    }

}


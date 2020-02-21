package com.tuofan.dingding.web;


import com.tuofan.dingding.entity.DingUser;
import com.tuofan.dingding.service.IDingDeptService;
import com.tuofan.dingding.service.IDingUserService;
import com.tuofan.configs.service.ConfigCachedUtils;
import com.tuofan.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/bill/teacher")
public class TeacherController {


    @Autowired
    public IDingDeptService iDingDeptService;

    @Autowired
    public IDingUserService iDingUserService;

    @Autowired
    public ConfigCachedUtils configCachedUtils;


    @PostMapping("listAll")
    public Result listAll() {
        return Result.ok(iDingUserService.list());
    }

    @PostMapping("update")
    public Result update(DingUser dingUser) {
        DingUser saved = iDingUserService.getById(dingUser.getId());
        if (saved != null) {
            if (dingUser.getIsAppAdmin() != null) saved.setIsAppAdmin(1);
            if (dingUser.getIsTeacher() != null) saved.setIsTeacher(dingUser.getIsTeacher());
        }
        return Result.ok();
    }

}

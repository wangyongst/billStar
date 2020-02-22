package com.tuofan.orgination.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.orgination.entity.DingUser;
import com.tuofan.orgination.service.IDingDeptService;
import com.tuofan.orgination.service.IDingUserService;
import com.tuofan.orgination.vo.TeacherQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bill/teacher")
public class TeacherController {


    @Autowired
    public IDingDeptService iDingDeptService;

    @Autowired
    public IDingUserService iDingUserService;


    @PostMapping("page")
    public Result page(@RequestBody TeacherQ teacherQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (teacherQ.getNameLike() != null) queryWrapper.like("teacher.name", teacherQ.getNameLike());
        if (!CollectionUtils.isEmpty(teacherQ.getDeptIds())) queryWrapper.in("school.id", teacherQ.getDeptIds());
        return Result.ok(iDingUserService.pageTeacher(new Page(teacherQ.getCurrent(), teacherQ.getSize()), queryWrapper));
    }

    @PostMapping("update")
    public Result update(@RequestBody DingUser dingUser) {
        DingUser saved = iDingUserService.getById(dingUser.getId());
        if (saved != null) {
            if (dingUser.getIsAppAdmin() != null) saved.setIsAppAdmin(dingUser.getIsAppAdmin());
            if (dingUser.getIsTeacher() != null) saved.setIsTeacher(dingUser.getIsTeacher());
            iDingUserService.saveOrUpdate(saved);
        }
        return Result.ok();
    }

}

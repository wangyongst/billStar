package com.tuofan.orgination.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.orgination.entity.DingDept;
import com.tuofan.orgination.entity.DingUser;
import com.tuofan.orgination.service.IDingDeptService;
import com.tuofan.orgination.service.IDingUserService;
import com.tuofan.orgination.vo.TeacherQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("listTeacher")
    public Result listAllTeacher(@RequestBody TeacherQ teacherQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("teacher.is_teacher", 1);
        if (teacherQ.getSchoolId() != null && teacherQ.getSchoolId() != 0) queryWrapper.eq("mid.dept_id", teacherQ.getSchoolId());
        return Result.ok(iDingUserService.listAllTeacher(queryWrapper));
    }

    @GetMapping("listAllTeacher")
    public Result listAllTeacher() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("teacher.is_teacher", 1);
        return Result.ok(iDingUserService.listAllTeacher(queryWrapper));
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

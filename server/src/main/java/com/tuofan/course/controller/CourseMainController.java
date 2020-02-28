package com.tuofan.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.Result;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.entity.CourseTime;
import com.tuofan.course.service.ICourseTimeService;
import com.tuofan.course.vo.CourseP;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.course.vo.CourseQ;
import com.tuofan.orgination.vo.TeacherQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程信息 前端控制器
 * </p>
 *
 * @author wangyong
 * @since 2020-02-23
 */
@RestController
@RequestMapping("/bill/course/main")
public class CourseMainController {

    @Autowired
    private ICourseMainService iCourseMainService;

    @Autowired
    private ICourseTimeService iCourseTimeService;

    @PostMapping("list")
    public Result list(@RequestBody CourseQ courseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(courseQ.getSemesterIds())) queryWrapper.eq("semester.id", courseQ.getSemesterId());
        if (!CollectionUtils.isEmpty(courseQ.getSubjectIds())) queryWrapper.eq("school.id", courseQ.getSchoolId());
        return Result.ok(iCourseMainService.listV(queryWrapper));
    }

    @PostMapping("page")
    public Result page(@RequestBody CourseQ courseQ) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (!CollectionUtils.isEmpty(courseQ.getSemesterIds())) queryWrapper.in("semester.id", courseQ.getSemesterIds());
        if (!CollectionUtils.isEmpty(courseQ.getSubjectIds())) queryWrapper.in("subject.id", courseQ.getSubjectIds());
        if (!CollectionUtils.isEmpty(courseQ.getSchoolIds())) queryWrapper.in("school.id", courseQ.getSchoolIds());
        return Result.ok(iCourseMainService.pageV(new Page(courseQ.getCurrent(), courseQ.getSize()), queryWrapper));
    }


    @PostMapping("create")
    public Result create(@RequestBody CourseP courseP) {
        if (courseP.getType() == 1) {
            iCourseTimeService.save(courseP.getDay());
            courseP.setTimeIds(courseP.getDay().getId().toString());
        } else if (courseP.getType() == 2) {
            StringBuffer ids = new StringBuffer();
            for (CourseTime t : courseP.getDayList()) {
                if (t.getBegin() != null && t.getEnd() != null) {
                    iCourseTimeService.save(t);
                    ids.append(t.getId()).append(",");
                }
            }
            courseP.setTimeIds(ids.deleteCharAt(ids.length() - 1).toString());
        }
        iCourseMainService.save(courseP);
        return Result.ok("保存成功");
    }

}


package com.tuofan.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tuofan.core.CheckUtils;
import com.tuofan.core.Result;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.entity.CourseTime;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.course.service.ICourseTimeService;
import com.tuofan.course.vo.CourseP;
import com.tuofan.course.vo.CourseQ;
import com.tuofan.orgination.service.IDingUserService;
import com.tuofan.setting.service.ISysClassNoService;
import com.tuofan.setting.service.ISysClassRoomService;
import com.tuofan.setting.service.ISysClassService;
import com.tuofan.student.service.IStudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

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

    @Autowired
    private IDingUserService iDingUserService;

    @Autowired
    private ISysClassService iSysClassService;

    @Autowired
    private ISysClassNoService iSysClassNoService;

    @Autowired
    private ISysClassRoomService iSysClassRoomService;

    @Autowired
    private IStudentCourseService iStudentCourseService;

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

    @PostMapping("delete")
    public Result delete(@RequestParam Integer id) {
        CourseMain course = iCourseMainService.getById(id);
        QueryWrapper query = new QueryWrapper();
        query.eq("course_id", id);
        if (iStudentCourseService.list(query).size() > 0) return Result.error("课程有学生，不能删除");
        iCourseMainService.removeById(id);
        return Result.ok();
    }


    @PostMapping("create")
    public Result create(@RequestBody CourseP courseP) {
        if (courseP.getType() == 1 && courseP.getDay() != null) {
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
        if (CheckUtils.isNotZero(courseP.getTeacherId())) courseP.setTeacherName(iDingUserService.getById(courseP.getTeacherId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassId())) courseP.setClassName(iSysClassService.getById(courseP.getClassId()).getName());
        StringBuffer stringBuffer = new StringBuffer();
        for (CourseTime ct : iCourseTimeService.listByIds(Arrays.asList(courseP.getTimeIds().split(",")))) {
            stringBuffer.append(ct.getDay() + ":" + DateTimeUtils.formatTime(ct.getBegin()) + "-" + DateTimeUtils.formatTime(ct.getEnd()));
            stringBuffer.append("+\n");
        }
        courseP.setCourseTime(stringBuffer.toString());
        if (CheckUtils.isNotZero(courseP.getClassNoId())) courseP.setClassNo(iSysClassNoService.getById(courseP.getClassNoId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassRoomId())) courseP.setClassRoom(iSysClassNoService.getById(courseP.getClassRoomId()).getName());
        iCourseMainService.save(courseP);
        return Result.ok("保存成功");
    }

    @PostMapping("update")
    public Result update(@RequestBody CourseP courseP) {
        if (courseP.getType() == 1 && courseP.getDay() != null) {
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
        if (CheckUtils.isNotZero(courseP.getTeacherId())) courseP.setTeacherName(iDingUserService.getById(courseP.getTeacherId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassId())) courseP.setClassName(iSysClassService.getById(courseP.getClassId()).getName());
        StringBuffer stringBuffer = new StringBuffer();
        for (CourseTime ct : iCourseTimeService.listByIds(Arrays.asList(courseP.getTimeIds().split(",")))) {
            stringBuffer.append(ct.getDay() + ":" + DateTimeUtils.formatTime(ct.getBegin()) + "-" + DateTimeUtils.formatTime(ct.getEnd()));
            stringBuffer.append("+\n");
        }
        courseP.setCourseTime(stringBuffer.toString());
        if (CheckUtils.isNotZero(courseP.getClassNoId())) courseP.setClassNo(iSysClassNoService.getById(courseP.getClassNoId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassRoomId())) courseP.setClassRoom(iSysClassNoService.getById(courseP.getClassRoomId()).getName());
        iCourseMainService.updateById(courseP);
        return Result.ok("修改成功");
    }
}


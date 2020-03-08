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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        deleteCourseTime(course);
        iCourseMainService.removeById(id);
        return Result.ok();
    }


    @PostMapping("create")
    public Result create(@RequestBody CourseP courseP) {
        courseP = makeCourse(courseP);
        Result result = checkCourseNot(courseP);
        if (result.getCode() == 0) return result;
        result = checkCourseDuplicate(courseP);
        if (result.getCode() == 0) return result;
        result = checkTimeDuplicate(courseP);
        if (result.getCode() == 0) return result;
        courseP = addCourseTime(courseP);
        iCourseMainService.save(courseP);
        return Result.ok("保存成功");
    }

    @PostMapping("update")
    public Result update(@RequestBody CourseP courseP) {
        courseP = makeCourse(courseP);
        Result result = checkCourseNot(courseP);
        if (result.getCode() == 0) return result;
        result = checkCourseDuplicate(courseP);
        if (result.getCode() == 0) return result;
        result = checkTimeDuplicate(courseP);
        if (result.getCode() == 0) return result;
        deleteCourseTime(iCourseMainService.getById(courseP.getId()));
        courseP = addCourseTime(courseP);
        iCourseMainService.updateById(courseP);
        return Result.ok("修改成功");
    }

    public CourseP makeCourse(CourseP courseP) {
        if (CheckUtils.isNotZero(courseP.getTeacherId())) courseP.setTeacherName(iDingUserService.getById(courseP.getTeacherId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassId())) courseP.setClassName(iSysClassService.getById(courseP.getClassId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassNoId())) courseP.setClassNo(iSysClassNoService.getById(courseP.getClassNoId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassRoomId())) courseP.setClassRoom(iSysClassRoomService.getById(courseP.getClassRoomId()).getName());
        return courseP;
    }

    public CourseP addCourseTime(CourseP courseP) {
        if (courseP.getType() == 1 && courseP.getDay() != null) {
            if (courseP.getDay().getBegin() != null && courseP.getDay().getEnd() != null) {
                iCourseTimeService.save(courseP.getDay());
                courseP.setTimeIds(courseP.getDay().getId().toString());
            }
        } else if (courseP.getType() == 2) {
            StringBuffer ids = new StringBuffer();
            for (CourseTime t : courseP.getDayList()) {
                if (t.getBegin() != null && t.getEnd() != null) {
                    iCourseTimeService.save(t);
                    ids.append(t.getId()).append(",");
                }
            }
            courseP.setTimeIds(ids.toString());
        }
        if (StringUtils.isBlank(courseP.getTimeIds())) return courseP;
        StringBuffer stringBuffer = new StringBuffer();
        for (CourseTime ct : iCourseTimeService.listByIds(Arrays.asList(courseP.getTimeIds().split(",")))) {
            if (StringUtils.isNotBlank(ct.getDay()) && ct.getBegin() != null && ct.getEnd() != null) {
                stringBuffer.append(ct.getDay() + ":" + DateTimeUtils.formatTimeHourminter(ct.getBegin()) + "-" + DateTimeUtils.formatTimeHourminter(ct.getEnd()));
                stringBuffer.append("+\n");
            }
        }
        if (stringBuffer.length() > 1) courseP.setCourseTime(stringBuffer.substring(0, stringBuffer.length() - 1));
        return courseP;
    }

    public void deleteCourseTime(CourseMain courseMain) {
        QueryWrapper query = new QueryWrapper();
        query.in("id", courseMain.getTimeIds());
        iCourseTimeService.remove(query);
    }

    public Result checkCourseNot(CourseP courseP) {
        if (CheckUtils.isZero(courseP.getSemesterId())) return Result.error("学期是必填项");
        if (CheckUtils.isZero(courseP.getClassId())) return Result.error("班级是必填项");
        if (CheckUtils.isZero(courseP.getSchoolId())) return Result.error("学区是必填项");
        if (CheckUtils.isZero(courseP.getTeacherId())) return Result.error("教师是必填项");
        return Result.ok();
    }

    public Result checkCourseDuplicate(CourseP courseP) {
        QueryWrapper<CourseMain> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .or(e -> e.eq(CourseMain::getClassId, courseP.getClassId()))
                .or(e -> e.eq(CourseMain::getTeacherId, courseP.getTeacherId()))
                .or(e -> e.eq(CourseMain::getClassNoId, courseP.getClassNoId()))
                .or(e -> e.eq(CourseMain::getClassRoomId, courseP.getClassRoomId()));
        List<CourseMain> list = iCourseMainService.list(queryWrapper);
        if (CheckUtils.isNotZero(courseP.getId())) {
            list = list.stream().filter(e -> e.getId() != courseP.getId()).collect(Collectors.toList());
        }
        if (list.size() > 0) return Result.error("课程信息不能重复");
        return Result.ok();
    }

    public Result checkTimeDuplicate(CourseP courseP) {
        if (courseP.getType() == 1 && courseP.getDay() != null) {
            if (courseP.getDay().getBegin() != null && courseP.getDay().getEnd() != null) {
                QueryWrapper<CourseTime> queryWrapper = new QueryWrapper();
                queryWrapper.lambda()
                        .or(e -> e.le(CourseTime::getBegin, courseP.getDay().getEnd()))
                        .or(e -> e.ge(CourseTime::getEnd, courseP.getDay().getBegin()));
                List<CourseTime> list = iCourseTimeService.list(queryWrapper);
                if (CheckUtils.isNotZero(courseP.getId())) {
                    list = list.stream().filter(e -> !Arrays.asList(courseP.getTimeIds().split(",")).contains(e)).collect(Collectors.toList());
                }
                if (list.size() > 0) return Result.error("课程时间不能重复");
            }
        } else if (courseP.getType() == 2) {
            for (CourseTime t : courseP.getDayList()) {
                if (t.getBegin() != null && t.getEnd() != null) {
                    QueryWrapper<CourseTime> queryWrapper = new QueryWrapper();
                    queryWrapper.lambda()
                            .or(e -> e.le(CourseTime::getBegin, courseP.getDay().getEnd()))
                            .or(e -> e.ge(CourseTime::getEnd, courseP.getDay().getBegin()));
                    List<CourseTime> list = iCourseTimeService.list(queryWrapper);
                    if (CheckUtils.isNotZero(courseP.getId())) {
                        list = list.stream().filter(e -> !Arrays.asList(courseP.getTimeIds().split(",")).contains(e)).collect(Collectors.toList());
                    }
                    list = list.stream().filter(e -> e.getDay() != t.getDay()).collect(Collectors.toList());
                    if (list.size() > 0) return Result.error("课程时间不能重复");
                }
            }
        }
        return Result.ok();
    }
}


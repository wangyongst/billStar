package com.tuofan.course.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tuofan.core.CheckUtils;
import com.tuofan.core.Result;
import com.tuofan.core.utils.DateTimeUtils;
import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.entity.CourseTime;
import com.tuofan.course.service.ICourseMainService;
import com.tuofan.course.service.ICourseTimeService;
import com.tuofan.course.vo.CourseCount;
import com.tuofan.course.vo.CourseP;
import com.tuofan.course.vo.CourseQ;
import com.tuofan.orgination.service.IDingUserService;
import com.tuofan.setting.entity.SysClass;
import com.tuofan.setting.entity.SysSubject;
import com.tuofan.setting.service.ISysClassNoService;
import com.tuofan.setting.service.ISysClassRoomService;
import com.tuofan.setting.service.ISysClassService;
import com.tuofan.setting.service.ISysSubjectService;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.service.IStudentCourseService;
import com.tuofan.student.service.IStudentMainService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
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

    @Autowired
    private IStudentMainService iStudentMainService;

    @Autowired
    private ISysSubjectService iSysSubjectService;


    @GetMapping("count")
    public Result count() {
        CourseCount courseCount = new CourseCount();
        QueryWrapper query = new QueryWrapper();
        query.eq("type", 0);
        courseCount.setStudentTotal(iStudentMainService.count(query));
        List<StudentCourse> studentCourseList = iStudentCourseService.list();
        List<CourseMain> courseMainList = iCourseMainService.list();
        courseCount.setCourseTotal(studentCourseList.size());
        List<SysSubject> subjectList = iSysSubjectService.list();
        List<SysClass> classList = iSysClassService.list();
        courseCount.setMeishuTotal(countNumber(subjectList, classList, courseMainList, studentCourseList, "美术"));
        courseCount.setShufaTotal(countNumber(subjectList, classList, courseMainList, studentCourseList, "书法"));
        courseCount.setMankeTotal(countManke(courseMainList, studentCourseList));
        courseCount.setManbanlv(manbanlv(courseMainList));
        return Result.ok(courseCount);
    }

    public long countNumber(List<SysSubject> subjectList, List<SysClass> classList, List<CourseMain> courseMainList, List<StudentCourse> studentCourseList, String name) {
        List<Integer> subjectIds = subjectList.stream().filter(e -> e.getName().equals(name)).map(e -> e.getId()).collect(Collectors.toList());
        List<Integer> classIds = classList.stream().filter(e -> subjectIds.contains(e.getSubjectId())).map(e -> e.getId()).collect(Collectors.toList());
        List<Integer> courseIds = courseMainList.stream().filter(e -> classIds.contains(e.getClassId())).map(e -> e.getId()).collect(Collectors.toList());
        return studentCourseList.stream().filter(e -> courseIds.contains(e.getCourseId())).count();
    }

    public long countManke(List<CourseMain> courseMainList, List<StudentCourse> studentCourseList) {
        List<Integer> courseIds = courseMainList.stream().filter(e -> e.getStudentNum() != null && e.getStudentNum() >= e.getClassNum()).map(e -> e.getId()).collect(Collectors.toList());
        return studentCourseList.stream().filter(e -> courseIds.contains(e.getCourseId())).count();
    }

    public String manbanlv(List<CourseMain> courseMainList) {
        int studentNum = courseMainList.stream().mapToInt(CourseMain::getStudentNum).sum();
        int classNum = courseMainList.stream().mapToInt(CourseMain::getClassNum).sum();
        DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
        return String.valueOf(df.format((float)studentNum*100/classNum)+"%");
    }

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
        Result result = checkCourseNot(courseP);
        if (result.getCode() == 0) return result;
        result = checkClassNoNotMe(courseP);
        if (result.getCode() == 0) return result;
        courseP = makeCourseTime(courseP);
        if (CollectionUtils.isEmpty(courseP.getDayList())) return Result.error("上课时间是必填项");
        result = checkTeacherAndTime(courseP);
        if (result.getCode() == 0) return result;
        result = checkClassNoAndTime(courseP);
        if (result.getCode() == 0) return result;
        courseP = makeCourse(courseP);
        courseP = addCourseTime(courseP);
        iCourseMainService.save(courseP);
        return Result.ok("保存成功");
    }

    @PostMapping("update")
    public Result update(@RequestBody CourseP courseP) {
        Result result = checkCourseNot(courseP);
        if (result.getCode() == 0) return result;
        result = checkClassNoNotMe(courseP);
        if (result.getCode() == 0) return result;
        courseP = makeCourseTime(courseP);
        if (CollectionUtils.isEmpty(courseP.getDayList())) return Result.error("上课时间是必填项");
        result = checkTeacherAndTime(courseP);
        if (result.getCode() == 0) return result;
        result = checkClassNoAndTime(courseP);
        if (result.getCode() == 0) return result;
        courseP = makeCourse(courseP);
        deleteCourseTime(iCourseMainService.getById(courseP.getId()));
        courseP = addCourseTime(courseP);
        iCourseMainService.updateById(courseP);
        return Result.ok("修改成功");
    }

    public CourseP makeCourseTime(CourseP courseP) {
        List<CourseTime> courseTimeList = Lists.newArrayList();
        if (courseP.getType() == 1 && courseP.getDay() != null && courseP.getDay().getBegin() != null && courseP.getDay().getEnd() != null) {
            courseTimeList.add(courseP.getDay());
        } else if (courseP.getType() == 2) {
            for (CourseTime t : courseP.getDayList()) {
                if (t.getBegin() != null && t.getEnd() != null) {
                    courseTimeList.add(t);
                }
            }
        }
        courseP.setDayList(courseTimeList);
        return courseP;
    }

    public CourseP makeCourse(CourseP courseP) {
        if (CheckUtils.isNotZero(courseP.getTeacherId())) courseP.setTeacherName(iDingUserService.getById(courseP.getTeacherId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassId())) courseP.setClassName(iSysClassService.getById(courseP.getClassId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassNoId())) courseP.setClassNo(iSysClassNoService.getById(courseP.getClassNoId()).getName());
        if (CheckUtils.isNotZero(courseP.getClassRoomId())) courseP.setClassRoom(iSysClassRoomService.getById(courseP.getClassRoomId()).getName());
        return courseP;
    }

    public CourseP addCourseTime(CourseP courseP) {
        StringBuffer courseTime = new StringBuffer();
        StringBuffer timeIds = new StringBuffer();
        if (courseP.getDayList().size() == 0) return courseP;
        iCourseTimeService.saveBatch(courseP.getDayList());
        for (CourseTime ct : courseP.getDayList()) {
            timeIds.append(ct.getId());
            timeIds.append(",");
            courseTime.append(ct.getDay() + ":" + ct.getBegin().format(DateTimeFormatter.ofPattern("HH:mm")) + "-" + ct.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
            courseTime.append("+\n");
        }
        courseP.setCourseTime(courseTime.substring(0, courseTime.length() - 2));
        courseP.setTimeIds(timeIds.substring(0, timeIds.length() - 1));
        return courseP;
    }

    public void deleteCourseTime(CourseMain courseMain) {
        if (StringUtils.isBlank(courseMain.getTimeIds())) return;
        QueryWrapper query = new QueryWrapper();
        query.in("id", Arrays.asList(courseMain.getTimeIds().split(",")));
        iCourseTimeService.remove(query);
    }

    public Result checkCourseNot(CourseP courseP) {
        if (CheckUtils.isZero(courseP.getSemesterId())) return Result.error("学期是必填项");
        if (CheckUtils.isZero(courseP.getClassId())) return Result.error("班级是必填项");
        if (CheckUtils.isZero(courseP.getSchoolId())) return Result.error("学区是必填项");
        if (CheckUtils.isZero(courseP.getTeacherId())) return Result.error("教师是必填项");
        if (CheckUtils.isZero(courseP.getClassRoomId())) return Result.error("教室是必填项");
        if (CheckUtils.isZero(courseP.getClassNoId())) return Result.error("班别是必填项");
        return Result.ok();
    }

    //3、相同班级和科目的情况下，班别不能相同。
    public Result checkClassNoNotMe(CourseP courseP) {
        QueryWrapper<CourseMain> queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_id", courseP.getClassId());
        queryWrapper.eq("class_no_id", courseP.getClassNoId());
        List<CourseMain> list = iCourseMainService.list(queryWrapper);
        if (CheckUtils.isNotZero(courseP.getId())) {
            list = list.stream().filter(e -> e.getId() != courseP.getId()).collect(Collectors.toList());
        }
        if (list.size() > 0) return Result.error("相同班级和科目的情况下，班别不能相同");
        return Result.ok();
    }

    //2、同一个教室在同一个时间段，只能有一个班。
    public Result checkClassNoAndTime(CourseP courseP) {
        StringBuffer ids = new StringBuffer();
        listClassRoomDuplicateNotMe(courseP).forEach(e -> ids.append(e.getTimeIds()).append(","));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id", Arrays.asList(ids.toString().split(",")));
        List<CourseTime> courseTimes = iCourseTimeService.list(queryWrapper);
        List<CourseTime> courseTimeList = checkTimeDuplicateNotMe(courseP);
        List list = courseTimeList.stream().filter(e -> courseTimes.contains(e)).collect(Collectors.toList());
        if (list.size() > 0) return Result.error("同一个教室在同一个时间段，只能有一个班");
        return Result.ok();
    }

    //1、同一个老师在同一个时间段，只能带一个课
    public Result checkTeacherAndTime(CourseP courseP) {
        StringBuffer ids = new StringBuffer();
        listTeacherDuplicateNotMe(courseP).forEach(e -> ids.append(e.getTimeIds()).append(","));
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("id", Arrays.asList(ids.toString().split(",")));
        List<CourseTime> courseTimes = iCourseTimeService.list(queryWrapper);
        List<CourseTime> courseTimeList = checkTimeDuplicateNotMe(courseP);
        List list = courseTimeList.stream().filter(e -> courseTimes.contains(e)).collect(Collectors.toList());
        if (list.size() > 0) return Result.error("同一个老师在同一个时间段，只能带一个课");
        return Result.ok();
    }

    public List<CourseMain> listClassRoomDuplicateNotMe(CourseP courseP) {
        QueryWrapper<CourseMain> queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_room_id", courseP.getClassRoomId());
        return iCourseMainService.list(queryWrapper).stream().filter(e -> e.getId() != courseP.getId()).collect(Collectors.toList());
    }


    public List<CourseTime> checkTimeDuplicateNotMe(CourseP courseP) {
        List<CourseTime> courseTimes = Lists.newArrayList();
        for (CourseTime t : courseP.getDayList()) {
            QueryWrapper<CourseTime> queryWrapper = new QueryWrapper();
            queryWrapper.lambda()
                    .or(e -> e.le(CourseTime::getBegin, t.getBegin()).ge(CourseTime::getEnd, t.getBegin()))
                    .or(e -> e.le(CourseTime::getBegin, t.getEnd()).ge(CourseTime::getEnd, t.getEnd()));
            List<CourseTime> list = iCourseTimeService.list(queryWrapper);
            list = list.stream().filter(e -> e.getDay().equals(t.getDay()) || e.getType() == 1).collect(Collectors.toList());
            if (list.size() > 0) courseTimes.addAll(list);
        }
        if (CheckUtils.isNotZero(courseP.getId())) {
            courseTimes = courseTimes.stream().filter(e -> !Arrays.asList(courseP.getTimeIds().split(",")).contains(e)).collect(Collectors.toList());
        }
        return courseTimes;
    }


    public List<CourseMain> listTeacherDuplicateNotMe(CourseP courseP) {
        QueryWrapper<CourseMain> queryWrapper = new QueryWrapper();
        queryWrapper.eq("teacher_id", courseP.getTeacherId());
        List<CourseMain> list = iCourseMainService.list(queryWrapper);
        if (CheckUtils.isNotZero(courseP.getId())) list = list.stream().filter(e -> e.getId() != courseP.getId()).collect(Collectors.toList());
        return list;
    }
}


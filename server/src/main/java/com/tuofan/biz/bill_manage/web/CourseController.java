package com.tuofan.biz.bill_manage.web;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.entity.BillCourse;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.SysDict;
import com.tuofan.biz.bill_manage.service.BillCourseService;
import com.tuofan.biz.bill_manage.service.CourseService;
import com.tuofan.biz.bill_manage.service.SysDictService;
import com.tuofan.biz.bill_manage.vo.CourseQuery;
import com.tuofan.biz.bill_manage.vo.CreateCourseCmd;
import com.tuofan.biz.bill_manage.vo.UpdateCourseCmd;
import com.tuofan.biz.sys_orgnization.application.query.DingUserQueryService;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.dto.UserVO;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/course")
@Slf4j
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    private DingUserQueryService dingUserQueryService;

    @Autowired
    private DingDeptRepository dingDeptRepository;

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    BillCourseService billCourseService;

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("get")
    @LoginRequired
    @FieldConversion
    public Course get(@RequestParam Integer id) {
        return courseService.get(id);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @PostMapping("list")
    @LoginRequired
    @FieldConversion
    public List<Course> list(@RequestBody CourseQuery query) {
        List<Course> courses = courseService.listAllBy(query);
        courses.forEach(Course::calFillClassRate);
        return courses;
    }

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("listAllICanSee")
    @LoginRequired
    @FieldConversion
    public List<Course> listAllICanSee() {
        if (CollectionUtils.isEmpty(UserUtils.getLoginUserSchoolIds())) {
            return Lists.newArrayList();
        }
        return courseService.listByDeptIds(UserUtils.getLoginUserSchoolIds());
    }

    /**
     * 分页查询
     *
     * @return
     */
    @GetMapping("listCourses")
    @LoginRequired
    @FieldConversion
    public List<Course> listCourses(@RequestParam Integer deptSchoolId, @RequestParam Integer semesterId) {
        Course query = new Course();
        query.setDeptSchoolId(deptSchoolId);
        query.setSemesterId(semesterId);
        return courseService.listAll(query);
    }

    @GetMapping("delete")
    @LoginRequired
    public Integer delete(@RequestParam Integer id) {
        Course model = courseService.get(id);
        if (model == null) {
            throw BizException.locateByIdException();
        }
        BillCourse query = new BillCourse();
        query.setCourseId(id);
        List<BillCourse> list = billCourseService.listAll(query);
        if (!CollectionUtils.isEmpty(list)) {
            throw BizException.deleteFailForRefereed();
        }
        return courseService.delete(model);
    }

    @PostMapping("create")
    @LoginRequired
    public Integer create(@RequestBody @Valid CreateCourseCmd cmd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("100", bindingResult.getFieldError().getDefaultMessage());
        }
        this.validateCourseCmd(cmd);
        Course exist = courseService.locateBy(cmd.getSemesterId(), cmd.getDeptSchoolId(), cmd.getDictCourseId(), cmd.getCourseIndex(), cmd.getTeacherId());
        if (exist != null) {
            throw new BizException("200", "学期-校区-班级-班别-教师 课程已存在。");
        }
        // 开始创建
        Course course = ModelConvertHelper.convert(cmd, Course.class);
        course.setDeptSchoolId(cmd.getDeptSchoolId());
        return courseService.create(course);
    }

    @PostMapping("update")
    @LoginRequired
    public Integer update(@RequestBody @Valid UpdateCourseCmd updateCmd) {
        UserVO loginUser = UserUtils.getLoginUser();
        Course origin = courseService.get(updateCmd.getId());
        if (origin == null) {
            throw new BizException("100", "无法定位到课程");
        }
        if (!loginUser.getDeptSchoolIds().contains(origin.getDeptSchoolId())) {
            throw new BizException("100", "非法操作");
        }
        this.validateCourseCmd(updateCmd);
        Course exist = courseService.locateBy(origin.getSemesterId(), origin.getDeptSchoolId(), updateCmd.getDictCourseId(), updateCmd.getCourseIndex(), updateCmd.getTeacherId());
        if (exist != null && !exist.getId().equals(origin.getId())) {
            throw new BizException("200", "提交数据 学期-校区-班级-班别-教师 课程已存在。");
        }
        // 学期不让修改，避免和票据学期属性对不上
        updateCmd.setSemesterId(null);
        BeanUtils.copyProperties(updateCmd, origin);
        return courseService.update(origin);
    }

    private void validateCourseCmd(CreateCourseCmd updateCmd) {
//        UserVO loginUser = UserUtils.getLoginUser();
        SysDict classType = sysDictService.get(updateCmd.getDictCourseId());
        if (classType == null) {
            throw new BizException("100", "班级数据不合法");
        }
        //
        UserVO newTeacher = dingUserQueryService.getVO(updateCmd.getTeacherId());
        if (newTeacher == null) {
            throw new BizException("101", "无法定位到教师");
        }
        if (!newTeacher.getIsTeacher().equals(AppConstants.intYes)) {
            throw new BizException("101", "教师资格不合法");
        }
        if (CollectionUtils.isEmpty(newTeacher.getDeptSchoolIds())) {
            throw new BizException("101", "找不到课程教师的所属校区，请确认课程教师的所属校区");
        }
        if (!newTeacher.getDeptSchoolIds().contains(updateCmd.getDeptSchoolId())) {
            throw new BizException("101", "教师不为本校区教师");
        }
        //
        if (!Course.getCourseIndexMap().keySet().contains(updateCmd.getCourseIndex())) {
            throw new BizException("100", "班别数据不合法");
        }
    }


    @GetMapping("listClassIndex")
    public List<JSONObject> listClassIndex() {
        List<JSONObject> arr = Lists.newArrayList();
        for (Integer key : Course.getCourseIndexMap().keySet()) {
            JSONObject obj = new JSONObject();
            obj.put("id", key);
            obj.put("name", Course.getCourseIndexMap().get(key));
            arr.add(obj);
        }
        return arr;
    }

    @PostMapping("refreshCnt")
    public void refreshCnt(@RequestParam Integer courseId) {
        courseService.processCourseRealStudent(Lists.newArrayList(courseId));
    }


}

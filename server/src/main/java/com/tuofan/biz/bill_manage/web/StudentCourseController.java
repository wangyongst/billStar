package com.tuofan.biz.bill_manage.web;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.entity.StudentCourse;
import com.tuofan.biz.bill_manage.service.CourseService;
import com.tuofan.biz.bill_manage.service.StudentCourseService;
import com.tuofan.biz.bill_manage.vo.StudentCourseQuery;
import com.tuofan.biz.bill_manage.vo.StudentCourseVO;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.helper.ModelConvertHelper;
import jdk.nashorn.internal.runtime.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/studentCourse")
@Slf4j
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @Autowired
    CourseService courseService;

    /**
     * 分页查询
     *
     * @param request 请求体-参数
     * @return page数据
     */
    @PostMapping("listPage")
    @LoginRequired
    @FieldConversion
    public PageInfo<StudentCourseVO> listPage(@RequestBody PageRequest<StudentCourseQuery> request) {
        log.info("studentCourse listPage request={}", request);
        if (request == null) {
            request = new PageRequest<>();
        }
        if (request.getData() == null) {
            request.setData(new StudentCourseQuery());
        }
        PageInfo<StudentCourse> modelPage = studentCourseService.pageBy(request);
        PageInfo<StudentCourseVO> voPage = ModelConvertHelper.convertPageInfo(modelPage, StudentCourseVO.class);
        studentCourseService.doListCombine(voPage.getList(), false);
        return voPage;
    }

    @GetMapping("get")
    @LoginRequired
    @FieldConversion
    public StudentCourseVO get(@RequestParam Integer id) {
        log.info("studentCourse listPage id={}", id);
        StudentCourse studentCourse = studentCourseService.get(id);
        return studentCourseService.doEntityCombine(studentCourse);
    }

    @PostMapping("setAsDeActive")
    @LoginRequired
    public Integer setAsDeActive(@RequestParam Integer id) {
        StudentCourse studentCourse = studentCourseService.get(id);
        if (studentCourse == null) {
            return 0;
        }
        if (studentCourse.getIsActive() == null || studentCourse.getIsActive() == AppConstants.intYes) {
            studentCourse.setIsActive(AppConstants.intNo);
            Integer cnt = studentCourseService.update(studentCourse);
            courseService.processCourseRealStudent(Lists.newArrayList(studentCourse.getCourseId()));
            return cnt;
        }
        return 0;
    }


}

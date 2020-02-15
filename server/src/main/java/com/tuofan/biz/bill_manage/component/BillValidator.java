package com.tuofan.biz.bill_manage.component;

import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.service.BillService;
import com.tuofan.biz.bill_manage.service.CourseService;
import com.tuofan.biz.bill_manage.service.StudentService;
import com.tuofan.biz.bill_manage.vo.bill_new_student.CreateBillCourseCmd;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillValidator {

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Autowired
    BillService billService;

    public void validateDeptSchoolId(Integer deptSchoolId) {
        if (!UserUtils.getLoginUserSchoolIds().contains(deptSchoolId)) {
            throw new BizException("300", "您不能在非所属校区进行操作");
        }
    }

    /**
     * 抛异常形式阻断
     */
    public void validateCourse(List<CreateBillCourseCmd> coursesCmd, Integer deptSchoolID, Integer semesterId) {
        if (CollectionUtils.isEmpty(coursesCmd)) {
            throw new BizException("200", "缺少课程参数");
        }
        // check courses
        List<Integer> courseIds = coursesCmd.stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(courseIds)) {
            throw new BizException("200", "必须选择至少一门课程");
        }
        if (CollectionUtils.isEmpty(courseIds)) {
            throw new BizException("200", "必须选择至少一门课程");
        }
        List<Course> courses = courseService.listByIds(courseIds);
        if (CollectionUtils.isEmpty(courses)) {
            throw new BizException("200", "无法查到课程数据");
        }
        coursesCmd.forEach(ele -> {
            if (ele.getCourseId() != null && ele.getBeginTime() == null) {
                throw new BizException("200", "请输入课程开始时间");
            }
            if (ele.getCourseId() != null && ele.getExpireTime() == null) {
                throw new BizException("200", "请输入课程过期时间");
            }
        });
        courses.forEach(c -> {
            if (!c.getDeptSchoolId().equals(deptSchoolID)) {
                throw new BizException("200", "课程选择非法，不能跨校区选择课程");
            }
            if (!c.getSemesterId().equals(semesterId)) {
                throw new BizException("201", "课程选择非法，所属课程与学期关系不正确");
            }
        });
    }

    public void validateStudentUpdate(Integer studentId, String mobile, String name) {
        Student origin = studentService.get(studentId);
        if (origin == null) {
            throw new BizException("203", "无法根据ID定位到学生");
        }
        if (origin.getIsSuspended().equals(AppConstants.intYes)) {
            throw new BizException("200", "操作失败，该学生已休学");
        }
        Student exist = studentService.getByUniqueParam(origin.getDeptSchoolId(), name, mobile);
        if (exist != null && !exist.getId().equals(origin.getId())) {
            throw new BizException("202", "学生，校区+姓名+手机号已存在");
        }
    }

    public void validateStudentCreate(Integer deptSchoolId, String mobile, String name) {
        Student exist = studentService.getByUniqueParam(deptSchoolId, name, mobile);
        if (exist != null) {
            throw new BizException("201", "学生，校区+姓名+手机号已存在");
        }
    }

    public void validateOriginBill(Integer billId) {
        if (billId == null || billId < 0) {
            throw new BizException("2003", "需要提供原数据ID");
        }
        Bill origin = billService.get(billId);
        if (origin == null) {
            throw new BizException("2004", "无法定位到原数据");
        }
    }
}

package com.tuofan.biz.bill_manage.web;

import com.github.pagehelper.PageInfo;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.service.StudentService;
import com.tuofan.biz.bill_manage.vo.StudentQuery;
import com.tuofan.biz.bill_manage.vo.UpdateStudentCmd;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    @PostMapping("listPage")
    @LoginRequired
    @FieldConversion
    public PageInfo<Student> listPage(@RequestBody PageRequest<StudentQuery> request) {
        if (request == null) {
            request = new PageRequest<>();
        }
        if (request.getData() == null) {
            request.setData(new StudentQuery());
        }
        return studentService.listQueryPage(request);
//        studentService.combineArrears(students);
//        return pageInfo;
    }

    /**
     * 更新
     *
     * @param cmd
     * @return
     */
    @PostMapping("update")
    @LoginRequired
    public Integer update(@RequestBody @Valid UpdateStudentCmd cmd) {
        Student origin = studentService.get(cmd.getId());
        if (origin == null) {
            throw BizException.locateByIdException();
        }
        Student exam = studentService.getByUniqueParam(origin.getDeptSchoolId(), cmd.getName(), cmd.getMobile());
        if (exam != null && !exam.getId().equals(origin.getId())) {
            throw new BizException("100", "校区-姓名-手机号已存在，不允许重复");
        }
        BeanUtils.copyProperties(cmd, origin);
        return studentService.update(origin);
    }

    /**
     * 休学
     *
     * @param id
     * @return
     */
    @PostMapping("suspend")
    @LoginRequired
    public Integer suspend(@RequestParam Integer id) {
        Student origin = studentService.get(id);
        if (origin == null) {
            throw BizException.locateByIdException();
        }
        origin.setIsSuspended(AppConstants.intYes);
        return studentService.update(origin);
    }

    /**
     * 复学
     *
     * @param id
     * @return
     */
    @PostMapping("reinstate")
    @LoginRequired
    public Integer reinstate(@RequestParam Integer id) {
        Student origin = studentService.get(id);
        if (origin == null) {
            throw BizException.locateByIdException();
        }
        origin.setIsSuspended(AppConstants.intNo);
        return studentService.update(origin);
    }

    /**
     * 删除学生数据
     *
     * @param id
     * @return
     */
    @PostMapping("delete")
    @LoginRequired
    public Integer delete(@RequestParam Integer id) {
        Student origin = studentService.get(id);
        if (origin == null) {
            throw BizException.locateByIdException();
        }
        origin.setIsSuspended(AppConstants.intNo);
        return studentService.deleteWithBills(origin);
    }
}

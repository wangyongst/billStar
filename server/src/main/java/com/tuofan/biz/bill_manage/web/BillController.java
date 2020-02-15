package com.tuofan.biz.bill_manage.web;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.component.BillValidator;
import com.tuofan.biz.bill_manage.constants.BillTypes;
import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.Course;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.entity.StudentCourse;
import com.tuofan.biz.bill_manage.service.*;
import com.tuofan.biz.bill_manage.vo.BillForCmd;
import com.tuofan.biz.bill_manage.vo.BillQuery;
import com.tuofan.biz.bill_manage.vo.BillVO;
import com.tuofan.biz.bill_manage.vo.UpdateBillArrearsCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.CreateBillCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.CreateBillCourseCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.UpdateBillCmd;
import com.tuofan.biz.bill_manage.component.BillOperateMessageService;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.advice.convert.FieldConversion;
import com.tuofan.core.dto.PageRequest;
import com.tuofan.core.exception.BizException;
import com.tuofan.core.helper.ModelConvertHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Model;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/bill")
@Slf4j
public class BillController {


    @Autowired
    private BillQueryService billQueryService;


    @Autowired
    private BillCourseService billCourseService;

    @Autowired
    private StudentService studentService;

    /**
     * 根据studentCourseId 查询 studentId , 查询 courseId 得到 list BillVO
     *
     * @param studentCourseId 参数 studentCourseId
     * @return list BillVO
     */
    @GetMapping("listByStudentCourseId")
    @LoginRequired
    @FieldConversion
    public List<BillVO> listByStudentCourseId(@RequestParam Integer studentCourseId) {
        List<Bill> bills = billQueryService.listByStudentCourseId(studentCourseId);
        return billQueryService.processBillInformation(bills);
    }

    /**
     * 分页查询
     *
     * @param request 请求
     * @return 查询结果
     */
    @PostMapping("listPage")
    @LoginRequired
    @FieldConversion
    @SuppressWarnings("unchecked")
    public PageInfo<BillVO> listPage(@RequestBody PageRequest<BillQuery> request) {
        if (request == null) {
            request = new PageRequest<>();
        }
        if (request.getData() == null) {
            request.setData(new BillQuery());
        }
        PageInfo<Bill> billPage = billQueryService.listPageBy(request);
        PageInfo<BillVO> billVOPage = (PageInfo<BillVO>) ModelConvertHelper.convert(billPage, PageInfo.class);
        List<Bill> bills = billPage.getList();
        List<BillVO> billVOs = billQueryService.processBillInformation(bills);
        billVOPage.setList(billVOs);
        return billVOPage;
    }

    /**
     * 单个查询
     *
     * @param id 数据ID
     * @return 数据
     */
    @GetMapping("get")
    @LoginRequired
    public BillVO get(@RequestParam Integer id) {
        return billQueryService.getVO(id);
    }

    /**
     * 单个查询
     *
     * @param id 数据ID
     * @return 数据
     */
    @GetMapping("getForCmd")
    @LoginRequired
    public BillForCmd getForCmd(@RequestParam Integer id) {
        Bill bill = billQueryService.get(id);
        if (bill == null) {
            return null;
        }
        BillForCmd billForCmd = ModelConvertHelper.convert(bill, BillForCmd.class);
        billForCmd.setBillCourseList(billCourseService.listByBillId(id));
        billForCmd.setStudent(studentService.get(billForCmd.getStudentId()));
        return billForCmd;
    }


}

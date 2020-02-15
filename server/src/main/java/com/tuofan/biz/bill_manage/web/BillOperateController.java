package com.tuofan.biz.bill_manage.web;

import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.component.BillOperateMessageService;
import com.tuofan.biz.bill_manage.component.BillValidator;
import com.tuofan.biz.bill_manage.constants.BillTypes;
import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.service.BillService;
import com.tuofan.biz.bill_manage.service.CourseService;
import com.tuofan.biz.bill_manage.service.StudentCourseService;
import com.tuofan.biz.bill_manage.vo.UpdateBillArrearsCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.CreateBillCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.CreateBillCourseCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.UpdateBillCmd;
import com.tuofan.core.advice.auth.LoginRequired;
import com.tuofan.core.exception.BizException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Controller
@RequestMapping("${cert.api.prefix}/billOperate")
@Slf4j
public class BillOperateController {

    @Autowired
    private BillValidator billValidator;

    @Autowired
    private BillOperateMessageService billOperateMessageService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private BillService billService;

    @Autowired
    private StudentCourseService studentCourseService;

    @ApiOperation("开票")
    @PostMapping("createBill")
    @LoginRequired
    public Integer createBill(@RequestBody @Validated CreateBillCmd cmd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("300", bindingResult.getFieldError().getDefaultMessage());
        }
        if (cmd.getCurrentArrears() != null && cmd.getCurrentArrears() < 0) {
            throw new BizException("300", "欠费金额可以不填，但是不能小于0哦");
        }
        Integer targetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(targetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentCreate(targetDeptId, cmd.getStudent().getMobile(), cmd.getStudent().getName());
        // start course create
        Bill bill = billService.processCreate(cmd);
        billOperateMessageService.processBillMessage(bill.getId());
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        // 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }

    @ApiOperation("续费")
    @PostMapping("renewals")
    @LoginRequired
    public Integer renewals(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        cmd.initCurrentArrears();
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("200", bindingResult.getFieldError().getDefaultMessage());
        }
        if (cmd.getCurrentArrears() != null && cmd.getCurrentArrears() < 0) {
            throw new BizException("200", "欠费金额可以不填，但是不能小于0哦");
        }
        Integer targetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(targetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentUpdate(cmd.getStudent().getId(), cmd.getStudent().getMobile(), cmd.getStudent().getName());
        // start course create
        Bill bill = billService.processRenewals(cmd);
        billOperateMessageService.processBillMessage(bill.getId());
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        // 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }

    @ApiOperation("补费")
    @PostMapping("supplement")
    @LoginRequired
    public Integer supplement(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        cmd.initCurrentArrears();
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("2000", bindingResult.getFieldError().getDefaultMessage());
        }
        if (cmd.getModelId() == null) {
            throw new BizException("2001", "缺失原单据ID，请联系管理员");
        }
        Bill originModel = billService.get(cmd.getModelId());
        if (originModel == null) {
            throw new BizException("2002", "无法根据单据ID=" + cmd.getModelId() + "找到原单据");
        }
        Integer supplementTargetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(supplementTargetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), supplementTargetDeptId, cmd.getSemesterId());
        billValidator.validateStudentUpdate(cmd.getStudent().getId(), cmd.getStudent().getMobile(), cmd.getStudent().getName());
        // start course create
        Bill bill = billService.supplement(cmd, originModel);
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        // 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }

    @ApiOperation("退费")
    @PostMapping("refund")
    @LoginRequired
    public Integer refund(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        cmd.initCurrentArrears();
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("200", bindingResult.getFieldError().getDefaultMessage());
        }
        if (cmd.getAmount() >= 0) {
            throw new BizException("300", "退费金额需要为负数");
        }
        Integer targetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(targetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentUpdate(cmd.getStudent().getId(), cmd.getStudent().getMobile(), cmd.getStudent().getName());
        // start course create
        Bill bill = billService.refund(cmd);
        return bill.getId();
    }

    @ApiOperation("转班")
    @PostMapping("transferClass")
    @LoginRequired
    public Integer transferClass(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        cmd.initCurrentArrears();
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("2005", bindingResult.getFieldError().getDefaultMessage());
        }
        Integer targetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(targetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentUpdate(cmd.getStudent().getId(), cmd.getStudent().getMobile(), cmd.getStudent().getName());
        billValidator.validateOriginBill(cmd.getModelId());
        // start course create
        Bill bill = billService.transferClass(cmd);
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        // 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }

    @ApiOperation("更新票据信息")
    @PostMapping("updateBill")
    @LoginRequired
    public Integer updateBill(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("2007", bindingResult.getFieldError().getDefaultMessage());
        }
        Integer targetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(targetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentUpdate(cmd.getStudent().getId(), cmd.getStudent().getMobile(), cmd.getStudent().getName());
        billValidator.validateOriginBill(cmd.getModelId());
        // start course create
        Bill bill = billService.updateBillInfo(cmd);
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        // 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }


    @ApiOperation("转校")
    @PostMapping("transferSchool")
    @LoginRequired
    public Integer transferSchool(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("200", bindingResult.getFieldError().getDefaultMessage());
        }
        billValidator.validateOriginBill(cmd.getModelId());
        Integer originDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(originDeptId);
        Integer targetDeptId = cmd.getNewDeptSchoolId();
        if (originDeptId.equals(targetDeptId)) {
            throw new BizException("2003", "转校新校区不能是当前校区");
        }
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentCreate(targetDeptId, cmd.getStudent().getMobile(), cmd.getStudent().getName());
        // start course create
        Bill bill = billService.transferSchool(cmd);
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        //// 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }

    @ApiOperation("转期")
    @PostMapping("transferSemester")
    @LoginRequired
    public Integer transferSemester(@RequestBody @Validated UpdateBillCmd cmd, BindingResult bindingResult) {
        cmd.initCurrentArrears();
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("200", bindingResult.getFieldError().getDefaultMessage());
        }
        if (cmd.getCurrentArrears() != null && cmd.getCurrentArrears() < 0) {
            throw new BizException("201", "欠费金额可以不填，但是不能小于0哦");
        }
        Integer targetDeptId = cmd.getDeptSchoolId();
        billValidator.validateDeptSchoolId(targetDeptId);
        billValidator.validateCourse(cmd.getBillCourseList(), targetDeptId, cmd.getSemesterId());
        billValidator.validateStudentUpdate(cmd.getStudent().getId(), cmd.getStudent().getMobile(), cmd.getStudent().getName());
        // start course create
        Bill bill = billService.processTransferSemester(cmd);
        // 处理学生课程的数据
        studentCourseService.processStudentCourse(bill.getDeptSchoolId());
        // 处理当前学生课程的实际人数
        List<Integer> courseIds = cmd.getBillCourseList().stream().map(CreateBillCourseCmd::getCourseId).collect(Collectors.toList());
        courseService.processCourseRealStudent(courseIds);
        return bill.getId();
    }

    @ApiOperation("更新欠费信息")
    @PostMapping("updateArrears")
    @LoginRequired
    public Integer transferSchool(@RequestBody @Validated UpdateBillArrearsCmd cmd, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new BizException("200", bindingResult.getFieldError().getDefaultMessage());
        }
        Bill bill = billService.get(cmd.getModelId());
        if (bill == null) {
            throw new BizException("201", "无法根据ID=" + cmd.getModelId() + "定位到单据数据");
        }
        if (!Lists.newArrayList(BillTypes.NEW_BILL, BillTypes.RENEWALS).contains(bill.getType())) {
            throw new BizException("201", "仅有新生和续费单据才能修改欠费数据");
        }
        if (bill.getCurrentArrears() != null && bill.getCurrentArrears() > 0) {
            throw new BizException("201", "修改欠费仅针对欠费数据为零的单据");
        }
        billService.updateArrears(bill, cmd.getArrears());
        return bill.getId();
    }
}

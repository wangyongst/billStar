package com.tuofan.biz.bill_manage.service;

import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.constants.BillTypes;
import com.tuofan.biz.bill_manage.dao.BillDao;
import com.tuofan.biz.bill_manage.entity.Bill;
import com.tuofan.biz.bill_manage.entity.BillCourse;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.entity.StudentCourse;
import com.tuofan.biz.bill_manage.vo.bill_new_student.CreateBillCmd;
import com.tuofan.biz.bill_manage.vo.bill_new_student.UpdateBillCmd;
import com.tuofan.core.constants.AppConstants;
import com.tuofan.core.helper.ModelConvertHelper;
import com.tuofan.core.persistence.service.CrudRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService extends CrudRepository<BillDao, Bill> {

    @Autowired
    StudentService studentService;

    @Autowired
    BillCourseService billCourseService;

    @Autowired
    StudentCourseService studentCourseService;

    /**
     * 处理新建
     *
     * @param cmd 新建操作。
     * @return 新建的结果。
     */
    @Transactional(rollbackFor = Exception.class)
    public Bill processCreate(CreateBillCmd cmd) {
        // student
        Student student = ModelConvertHelper.convert(cmd.getStudent(), Student.class);
        student.setDeptSchoolId(cmd.getDeptSchoolId());
        student.setIsSuspended(AppConstants.intNo);
        student.setCurrentArrears(cmd.getCurrentArrears());
        studentService.create(student);
        // bill
        Bill bill = Bill.newInstance(BillTypes.NEW_BILL, student.getId(), cmd);
        this.create(bill);
        // bill-course
        List<BillCourse> billCourses = ModelConvertHelper.convertList(cmd.getBillCourseList(), BillCourse.class);
        billCourses.forEach(ele -> ele.setBillId(bill.getId()));
        billCourseService.createList(billCourses);
        this.cacheContextBillData(bill, billCourses, student);
        return bill;
    }

    private void cacheContextBillData(Bill bill, List<BillCourse> billCourses, Student student) {
        // 把数据缓存起来,后面的操作用得到
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        request.setAttribute("bill", bill);
        request.setAttribute("billCourses", billCourses);
        request.setAttribute("student", student);
    }

    /**
     * 处理续费。
     *
     * @param cmd 续费的传入参数。
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Bill processRenewals(UpdateBillCmd cmd) {
        return this.updateStudentCreateBill(cmd, BillTypes.RENEWALS);
    }

    /**
     * 处理续费。
     *
     * @param cmd 续费的传入参数。
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Bill processTransferSemester(UpdateBillCmd cmd) {
        return this.updateStudentCreateBill(cmd, BillTypes.TRANSFER_SEMESTER);
    }

    /**
     * 补费
     *
     * @param cmd cmd
     * @return bill
     */
    @Transactional(rollbackFor = Exception.class)
    public Bill supplement(UpdateBillCmd cmd, Bill originBill) {
        // 处理补费单据提交
        Bill supplementBill = this.updateStudentCreateBill(cmd, BillTypes.SUPPLEMENT);
        // 处理原单据的补费后欠费金额计算
        this.processBillArrears(originBill, -1 * cmd.getAmount());
        return supplementBill;
    }

    private void processBillArrears(Bill bill, Double amount) {
        bill.processArrears(amount);
        this.update(bill);
    }


    /**
     * 退费。
     *
     * @param cmd cmd
     * @return bill
     */
    @Transactional(rollbackFor = Exception.class)
    public Bill refund(UpdateBillCmd cmd) {
        return this.updateStudentCreateBill(cmd, BillTypes.REFUND);
    }


    /**
     * 更新学生，新增票据
     *
     * @param cmd  cmd
     * @param type type
     * @return bill
     */
    private Bill updateStudentCreateBill(UpdateBillCmd cmd, Integer type) {
        // bill
        Bill bill = Bill.newInstance(type, cmd.getStudent().getId(), cmd);
        bill.setModelId(cmd.getModelId());
        this.create(bill);
        // student
        Student student = studentService.get(cmd.getStudent().getId());
        BeanUtils.copyProperties(cmd.getStudent(), student);
        student.processArrears(cmd, type);
        studentService.update(student);
        // bill-course
        List<BillCourse> billCourses = ModelConvertHelper.convertList(cmd.getBillCourseList(), BillCourse.class);
        billCourses.forEach(ele -> ele.setBillId(bill.getId()));
        billCourseService.createList(billCourses);
        //处理bill数据
        this.cacheContextBillData(bill, billCourses, student);
        return bill;
    }

    /**
     * 转班
     *
     * @param cmd cmd
     * @return bill
     */
    public Bill transferClass(UpdateBillCmd cmd) {
        return this.updateBillInfo(cmd);
    }

    /**
     * 更新票据
     *
     * @param cmd cmd
     * @return bill
     */
    @Transactional
    public Bill updateBillInfo(UpdateBillCmd cmd) {
        // student
        Student student = studentService.get(cmd.getStudent().getId());
        BeanUtils.copyProperties(cmd.getStudent(), student);
        studentService.update(student);
        // bill-updateStudentCreateBill
        Bill bill = this.get(cmd.getModelId());
        BeanUtils.copyProperties(cmd, bill);
        this.update(bill);
        // bill-course delete and create
        billCourseService.deleteByBillId(bill.getId());
        List<BillCourse> billCourses = ModelConvertHelper.convertList(cmd.getBillCourseList(), BillCourse.class);
        billCourses.forEach(ele -> ele.setBillId(bill.getId()));
        billCourseService.createList(billCourses);
        this.cacheContextBillData(bill, billCourses, student);
        return bill;
    }

    /**
     * 转校
     *
     * @param cmd cmd
     * @return bill
     */
    public Bill transferSchool(UpdateBillCmd cmd) {
        // student,origin data updateStudentCreateBill
        Student student = studentService.get(cmd.getStudent().getId());
        BeanUtils.copyProperties(cmd.getStudent(), student);
        studentService.update(student);
        // student new create
        Student newStudent = student;
        newStudent.setId(null);
        newStudent.setDeptSchoolId(cmd.getNewDeptSchoolId());
        studentService.create(student);
        // bill-updateStudentCreateBill
        Bill bill = this.get(cmd.getModelId());
        bill.setAmount(bill.getAmount() - cmd.getAmount());
        this.update(bill);
        // new bill - create
        Bill newBill = Bill.newInstance(BillTypes.TRANSFER_SCHOOL, newStudent.getId(), cmd);
        newBill.setDeptSchoolId(cmd.getNewDeptSchoolId());
        this.create(newBill);
        // new bill-course
        List<BillCourse> billCourses = ModelConvertHelper.convertList(cmd.getBillCourseList(), BillCourse.class);
        billCourses.forEach(ele -> ele.setBillId(newBill.getId()));
        billCourseService.createList(billCourses);
        // cache
        cacheContextBillData(bill, billCourses, student);
        return bill;
    }

    /**
     * 删除学生相关票据
     *
     * @param studentId 学生ID
     * @return 影响票据行数
     */
    public Integer deleteByStudentID(Integer studentId) {
        // bill
        Bill query = new Bill();
        query.setStudentId(studentId);
        List<Bill> list = this.listAll(query);
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }
        List<Integer> billIdList = list.stream().map(Bill::getId).collect(Collectors.toList());
        this.deleteList(list);
        // bill-course
        List<BillCourse> billCourses = billCourseService.listByBillIds(billIdList);
        if (!CollectionUtils.isEmpty(billCourses)) {
            billCourseService.deleteList(billCourses);
        }
        // student-course
        List<StudentCourse> studentCourses = studentCourseService.listByStudentId(studentId);
        if (CollectionUtils.isEmpty(studentCourses)) {
            studentCourseService.deleteList(studentCourses);
        }
        return list.size();
    }


    /**
     * 单独更新欠费
     *
     * @param bill    票据
     * @param arrears 欠费金额
     */
    @Transactional
    public void updateArrears(Bill bill, Double arrears) {
        bill.setCurrentArrears(arrears);
        bill.setInitialArrears(arrears);
        this.update(bill);
        Student student = studentService.get(bill.getStudentId());
        student.processArrears(arrears);
        studentService.update(student);
    }
}

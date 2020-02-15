package com.tuofan.biz.bill_manage.entity;

import com.google.common.collect.Lists;
import com.tuofan.biz.bill_manage.constants.BillTypes;
import com.tuofan.biz.bill_manage.vo.bill_new_student.UpdateBillCmd;
import com.tuofan.biz.sys_orgnization.domain.service.DingDeptRepository;
import com.tuofan.biz.sys_orgnization.domain.service.DingUserRepository;
import com.tuofan.core.advice.convert.Converted;
import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_student")
public class Student extends BaseEntity {

    private Integer deptSchoolId;//所属校区。

    private String name;//姓名。

    private String mobile;//手机号。

    private Integer gender;//1=男，2=女。

    private String schoolName;//学校；注意，该学校为学生就读学校。

    private String className;//班级。

    private String address;//住址。

    private Integer isSuspended;//0=未休学，1=已休学。

    //    not exist in database
    private Double currentArrears;

    @Transient
    @Converted(dependProperty = "createBy", bean = DingUserRepository.class)
    private String createByName;

    @Transient
    @Converted(dependProperty = "updateBy", bean = DingUserRepository.class)
    private String updateByName;

    @Transient
    @Converted(dependProperty = "deptSchoolId", bean = DingDeptRepository.class)
    private String deptSchoolName;

    /**
     * 处理欠费
     *
     * @param amount 增加欠费就传正数。减少欠费就传负数。
     */
    public void processArrears(Double amount) {
        Double originArrears = this.getCurrentArrears() == null ? 0 : this.getCurrentArrears();
        Double targetArrears = originArrears + amount < 0 ? 0 : originArrears + amount;
        this.setCurrentArrears(targetArrears);
    }

    public void processArrears(UpdateBillCmd cmd, Integer type) {
        double amount = 0.0;
        if (Lists.newArrayList(BillTypes.NEW_BILL, BillTypes.RENEWALS).contains(type)) {
            amount = Math.abs(cmd.getCurrentArrears());
        } else if (Lists.newArrayList(BillTypes.SUPPLEMENT).contains(type)) {
            amount = -1 * Math.abs(cmd.getAmount());
        }
        this.processArrears(amount);
    }

}

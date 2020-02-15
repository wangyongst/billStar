package com.tuofan.biz.bill_manage.entity;

import com.tuofan.biz.bill_manage.vo.bill_new_student.BaseBillCmd;
import com.tuofan.core.persistence.entity.BaseEntity;
import com.tuofan.core.utils.UserUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_bill")
@Data
@EqualsAndHashCode(callSuper = true)
public class Bill extends BaseEntity {

    private String billNo;

    private Integer deptSchoolId;

    private Integer studentId;

    private Integer semesterId;

    private Double amount;

    private Double initialArrears; // 初始欠费。

    private Double currentArrears; // 当前欠费。每次补费后，会被更新。

    private Integer type;

    private Integer payTypeId;

    private Date billTime;

    private Integer billCreator;

    private String remark;

    private Integer isTransferred;

    private Integer modelId;

    public static Bill newInstance(Integer type, Integer studentId, BaseBillCmd cmd) {
        Bill bill = new Bill();
        bill.setType(type);
        bill.generateNo();
        bill.setAmount(cmd.getAmount());
        if (cmd.getCurrentArrears() == null) {
            bill.setCurrentArrears(0.0);
            bill.setInitialArrears(0.0);
        } else {
            bill.setCurrentArrears(cmd.getCurrentArrears());
            bill.setInitialArrears(cmd.getCurrentArrears());
        }
        bill.setSemesterId(cmd.getSemesterId());
        bill.setIsTransferred(cmd.getIsTransferred());
        bill.setRemark(cmd.getRemark());
        bill.setDeptSchoolId(cmd.getDeptSchoolId());
        bill.setStudentId(studentId);
        bill.setPayTypeId(cmd.getPayTypeId());
        if (cmd.getBillCreator() != null && cmd.getBillCreator() > 0) {
            bill.setBillCreator(cmd.getBillCreator());
            bill.setBillTime(cmd.getBillTime());
        } else {
            bill.setBillCreator(UserUtils.getLoginUser().getId());
            bill.setBillTime(new Date());
        }

        return bill;
    }

    private void generateNo() {
        this.billNo = String.format("%s%s%s",
                "P",
                Integer.toString(type),
                Long.toString(System.currentTimeMillis()));
    }

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
}

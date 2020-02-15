package com.tuofan.biz.bill_manage.vo;

import com.tuofan.biz.bill_manage.entity.BillCourse;
import com.tuofan.biz.bill_manage.entity.Student;
import com.tuofan.biz.bill_manage.vo.bill_new_student.BaseBillCmd;
import com.tuofan.core.persistence.entity.BaseEntity;
import com.tuofan.core.utils.UserUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
public class BillForCmd {

    private Integer id;

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

    private Student student;

    private List<BillCourse> billCourseList;

}

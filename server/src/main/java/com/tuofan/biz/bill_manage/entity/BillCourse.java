package com.tuofan.biz.bill_manage.entity;

import com.tuofan.core.persistence.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Table;
import java.util.Date;

@Table(name = "t_bill_course")
@Data
@EqualsAndHashCode(callSuper = true)
public class BillCourse extends BaseEntity {

    private Integer billId;

    private Integer courseId;

    private Date expireTime;// 过期时间

    private Date beginTime;// 开始时间

    private Date riseClassTime;// 升班时间

}

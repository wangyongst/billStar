package com.tuofan.biz.bill_manage.vo.bill_new_student;

import lombok.Data;

import java.util.Date;

@Data
public class CreateBillCourseCmd {

    private Integer courseId;

    private Date expireTime;

    private Date beginTime;// 开始时间

    private Date riseClassTime;// 升班时间

}

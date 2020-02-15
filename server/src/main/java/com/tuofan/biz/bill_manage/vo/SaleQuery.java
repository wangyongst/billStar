package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SaleQuery {

    private Date beginDate;

    private Date endDate;

    private List<Integer> deptSchoolIds;

}

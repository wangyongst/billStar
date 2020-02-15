package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BillCourseQuery {

    private Date expireEndDate;

    private List<Integer> courseIdList;

}

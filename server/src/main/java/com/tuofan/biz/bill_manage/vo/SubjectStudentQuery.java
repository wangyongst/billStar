package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SubjectStudentQuery {

    private Integer semesterId;

    private List<Integer> deptSchoolIds;

    private List<Integer> courseIds;

}

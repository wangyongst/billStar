package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.List;

@Data
public class StudentQuery {

    List<Integer> deptSchoolIds;

    private String mobileLike;

    private String nameLike;

    private Integer hasArrears;

    private boolean stopQuery = false;
}

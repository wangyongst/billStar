package com.tuofan.biz.bill_manage.vo;

import lombok.Data;

import java.util.Map;

@Data
public class SubjectStudentCountVO {

    private Boolean isTotal = false;

    private Integer deptSchoolId;

    private String deptSchoolName;

    private Map<String, SubjectCountVO> subjectStudentMap;

    private SubjectCountVO deptTotal;

}

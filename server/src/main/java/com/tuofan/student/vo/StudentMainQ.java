package com.tuofan.student.vo;

import com.tuofan.core.SearchQ;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentMainQ extends SearchQ {

    private List<String> teacherName;

    private Integer isarrears;

    private String mobileLike;

    private String nameLike;
}

package com.tuofan.student.vo;

import com.tuofan.core.SearchQ;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentChargeQ extends SearchQ {
    private List types;
    private Integer time;
    private Date begin;
    private Date end;
}

package com.tuofan.orgination.vo;

import com.tuofan.core.PageQ;
import lombok.Data;

import java.util.List;

@Data
public class TeacherQ extends PageQ {
    private List<Integer> deptIds;
    private String nameLike;
}

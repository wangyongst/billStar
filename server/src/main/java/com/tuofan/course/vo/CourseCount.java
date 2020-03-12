package com.tuofan.course.vo;

import lombok.Data;

import java.util.List;

@Data
public class CourseCount {

    private long studentTotal;

    private long courseTotal;

    private List<SubjectCount> subjectCounts;

    private long mankeTotal;

    private String manbanlv;

}

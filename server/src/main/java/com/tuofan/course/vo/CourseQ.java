package com.tuofan.course.vo;

import com.tuofan.core.PageQ;
import com.tuofan.course.entity.CourseMain;
import lombok.Data;

import java.util.List;

@Data
public class CourseQ extends PageQ {

    private List<Integer> schoolIds;

    private List<Integer> subjectIds;

    private List<Integer> semesterIds;

    private Integer semesterId;

    private Integer schoolId;

}

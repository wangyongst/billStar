package com.tuofan.course.vo;

import com.tuofan.core.PageQ;
import com.tuofan.course.entity.CourseMain;
import lombok.Data;

import java.util.List;

@Data
public class CourseQ extends PageQ {

    List<Integer> schoolIds;

    List<Integer> subjectIds;

    List<Integer> semesterIds;

}

package com.tuofan.course.vo;

import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.entity.CourseTime;
import lombok.Data;

import java.util.List;

@Data
public class CourseV extends CourseMain {

    private String schoolName;

    private String subjectName;
}

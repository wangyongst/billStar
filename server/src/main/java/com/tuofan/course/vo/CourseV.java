package com.tuofan.course.vo;

import com.tuofan.course.entity.CourseMain;
import lombok.Data;

@Data
public class CourseV extends CourseMain {

    private String schoolName;

    private String subjectName;

    private String className;

    private String teacherName;
}

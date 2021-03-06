package com.tuofan.student.vo;

import com.tuofan.core.PageQ;
import com.tuofan.student.entity.StudentCourse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentCourseV extends StudentCourse {

    private String schoolName;

    private String name;

    private String createName;

    private Date lostTime;

    private String mobile;

    private Float arrears;

    private String classNo;

    private String classRoom;

    private String className;

    private String teacherName;

    private String courseTime;

}

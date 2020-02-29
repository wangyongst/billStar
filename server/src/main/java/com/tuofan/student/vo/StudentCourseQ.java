package com.tuofan.student.vo;

import com.tuofan.core.PageQ;
import com.tuofan.core.SearchQ;
import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.entity.StudentMain;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentCourseQ extends SearchQ {

    private List<String> teacherName;

    private Integer days;

    private Date before;

    private Integer radio;
}

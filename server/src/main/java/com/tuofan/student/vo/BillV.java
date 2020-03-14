package com.tuofan.student.vo;

import com.tuofan.course.vo.CourseV;
import com.tuofan.student.entity.StudentCharge;
import lombok.Data;

import java.util.List;

@Data
public class BillV extends StudentCharge {

    private Integer schoolId;

    private String mainSchool;

    private String schoolName;

    private String schoolMobile;

    private String studentName;

    private String mobile;

    private String remarks;

    private String billTime;

    private String bigAmount;

    private List<StudentCourseV> courseList;
}

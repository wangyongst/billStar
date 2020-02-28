package com.tuofan.student.vo;

import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentCourse;
import lombok.Data;

import java.util.Date;

@Data
public class StudentChargeV extends StudentCharge {

    private String schoolName;

    private String studentName;

    private String createName;

    private String chargeName;
}

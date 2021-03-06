package com.tuofan.student.vo;

import com.tuofan.student.entity.StudentCharge;
import com.tuofan.student.entity.StudentCourse;
import com.tuofan.student.entity.StudentMain;
import lombok.Data;

import java.util.List;

@Data
public class ChargeV extends StudentCharge {

    private String schoolName;

    private String studentName;

    private String createName;

    private String studentname;
}

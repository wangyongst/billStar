package com.tuofan.student.vo;

import com.tuofan.student.entity.StudentMain;
import lombok.Data;

import java.util.Date;

@Data
public class StudentMainV extends StudentMain {

    private String schoolName;

    private String createName;

    private String value;
}

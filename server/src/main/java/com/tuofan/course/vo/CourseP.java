package com.tuofan.course.vo;

import com.tuofan.course.entity.CourseMain;
import com.tuofan.course.entity.CourseTime;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CourseP extends CourseMain {

    private int type;

    private CourseTime day;

    private List<CourseTime> dayList;
}

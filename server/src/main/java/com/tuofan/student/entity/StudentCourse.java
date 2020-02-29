package com.tuofan.student.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生-课程记录
 * </p>
 *
 * @author wangyong
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StudentCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生ID
     */
    private Integer studentId;

    /**
     * 课程ID
     */
    private Integer courseId;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 升班时间
     */
    private Date riseTime;

    /**
     * 到期时间
     */
    private Date expireTime;


    private String subjectName;


    private String className;


    private String classNo;


    private String classRoom;


    private String teacherName;


    private String courseTime;


}
